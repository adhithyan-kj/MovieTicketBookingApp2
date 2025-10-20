package movieticketbookinggui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Imports for PDFBox (required for PDF creation)
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/**
 * Advanced GUI implementing graphical seat layout, cart/summary,
 * Google-like color scheme, animation, and PDF export.
 */
public class BookingFrame extends JFrame {

    // --- 1. DECLARE COMPONENTS AND STATE ---

    // Google-like color palette
    private final Color PRIMARY_BLUE = new Color(66, 133, 244);
    private final Color SECONDARY_GREEN = new Color(52, 168, 83);
    private final Color DANGER_RED = new Color(234, 67, 53);
    private final Color AVAILABLE_SEAT_COLOR = new Color(240, 240, 240);

    private JComboBox<String> cmbShowtime;
    private JPanel pnlScreenIndicator;
    private JPanel pnlSeatLayout; // Graphical seat container
    private JTextArea txtBillSummary;
    private JLabel lblTotal;
    private JButton btnGeneratePdfBill;
    private JButton btnClearCart;
    private JLabel lblStatus;
    private String initialShowtimeKey;
    private JButton btnBack;

    // Animation/State Management
    private Timer statusTimer; // For blinking animation
    private boolean statusBlink = false;
    private Showtime currentShowtime;
    private List<Seat> selectedSeats = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(BookingFrame.class.getName());

    // --- 2. CONSTRUCTOR ---
    public BookingFrame(String initialKey) {
        this.initialShowtimeKey = initialKey;

        // UIManager is handled by MainApp.java

        initializeComponents();

        // Setup timer for blinking animation
        statusTimer = new Timer(300, this::statusTimerAction);
        statusTimer.setInitialDelay(0);

        // Calls loadShowtimes with the selected key
        loadShowtimes(initialKey);
        updateSeatMap();
        updateBillSummary();

        setTitle("Movie Ticket Booking");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Animation Action
    private void statusTimerAction(ActionEvent e) {
        if (statusBlink) {
            lblStatus.setForeground(DANGER_RED);
        } else {
            lblStatus.setForeground(Color.BLACK);
        }
        statusBlink = !statusBlink;
        if (!statusTimer.isRunning() && !statusBlink) {
            lblStatus.setForeground(Color.BLACK);
        }
    }

    // --- 3. UI ASSEMBLY METHOD ---
    private void initializeComponents() {
        setLayout(new BorderLayout(15, 15));

        // --- NORTH PANEL (Title, Selector & Back Button) ---
        // Use BorderLayout for the North container to place the Back button on the left (West)
        JPanel northContainer = new JPanel(new BorderLayout());

        // Center portion (Title and Selector)
        JPanel centerNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));

        JLabel titleLabel = new JLabel("Movie Booking");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_BLUE);

        cmbShowtime = new JComboBox<>();
        cmbShowtime.setPreferredSize(new Dimension(300, 35));
        cmbShowtime.addActionListener(this::cmbShowtimeActionPerformed);

        centerNorth.add(titleLabel);
        centerNorth.add(new JLabel("Select Show:"));
        centerNorth.add(cmbShowtime);

        northContainer.add(centerNorth, BorderLayout.CENTER); // Add center portion

        // Back Button (Placed on the West/Left side)
        btnBack = new JButton("← Back to Movies");
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.addActionListener(this::btnBackActionPerformed);

        // Add padding around the back button
        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        westWrapper.add(btnBack);

        northContainer.add(westWrapper, BorderLayout.WEST); // Add back button wrapper

        add(northContainer, BorderLayout.NORTH);

        // --- CENTER PANEL (Screen Indicator and Graphical Seats) ---

        // 1. Container for both Screen and Seats (uses BorderLayout)
        JPanel centerContainer = new JPanel(new BorderLayout());

        // 2. SCREEN INDICATOR (North of the center panel)
        pnlScreenIndicator = new JPanel();
        pnlScreenIndicator.setPreferredSize(new Dimension(500, 50));
        pnlScreenIndicator.setBackground(new Color(135, 206, 235)); // Light Sky Blue

        JLabel screenLabel = new JLabel("<< S C R E E N  I S  H E R E >>");
        screenLabel.setForeground(Color.DARK_GRAY);
        screenLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pnlScreenIndicator.add(screenLabel);
        centerContainer.add(pnlScreenIndicator, BorderLayout.NORTH);

        // 3. SEAT LAYOUT PANEL (Center of the center panel)
        pnlSeatLayout = new JPanel();
        pnlSeatLayout.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_BLUE),
                "THEATRE SEATING MAP (Click to Select)",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                PRIMARY_BLUE));

        JScrollPane centerScroll = new JScrollPane(pnlSeatLayout);
        centerContainer.add(centerScroll, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER); // Add main container to JFrame

        // --- EAST PANEL (Bill Summary / Cart) ---
        JPanel eastPanel = new JPanel(new BorderLayout(5, 10));
        eastPanel.setPreferredSize(new Dimension(320, 0));
        eastPanel.setBorder(BorderFactory.createTitledBorder("Your Cart & Bill"));

        txtBillSummary = new JTextArea(20, 25);
        txtBillSummary.setEditable(false);
        txtBillSummary.setFont(new Font("Monospaced", Font.PLAIN, 12));
        eastPanel.add(new JScrollPane(txtBillSummary), BorderLayout.CENTER);

        JPanel southEastPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        lblTotal = new JLabel("Total: Rs. 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(SECONDARY_GREEN);

        btnGeneratePdfBill = new JButton("DOWNLOAD BILL (PDF)");
        btnGeneratePdfBill.setBackground(SECONDARY_GREEN);
        btnGeneratePdfBill.setForeground(Color.WHITE);
        btnGeneratePdfBill.addActionListener(this::btnGeneratePdfBillActionPerformed);

        btnClearCart = new JButton("Clear Cart");
        btnClearCart.setBackground(DANGER_RED);
        btnClearCart.setForeground(Color.WHITE);
        btnClearCart.addActionListener(this::btnClearCartActionPerformed);

        southEastPanel.add(lblTotal);
        southEastPanel.add(btnGeneratePdfBill);
        southEastPanel.add(btnClearCart);

        eastPanel.add(southEastPanel, BorderLayout.SOUTH);
        add(eastPanel, BorderLayout.EAST);

        // --- SOUTH PANEL (Status Bar) ---
        lblStatus = new JLabel("Welcome to the advanced booking system.");
        lblStatus.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // --- NEW: PROJECT FOOTER & STATUS CONTAINER ---

// Create the Footer Label
        JLabel footerLabel = new JLabel("Created by Adhithyan K J and Team", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setForeground(Color.GRAY.darker());
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Padding

// Create a South container (using BorderLayout) to hold both Status and Footer
        JPanel southContainer = new JPanel(new BorderLayout());

// Add the Status Bar (lblStatus) to the NORTH of the container (this is the main status bar area)
        southContainer.add(lblStatus, BorderLayout.NORTH);

// Add the new Footer Label to the SOUTH of the container (the very bottom)
        southContainer.add(footerLabel, BorderLayout.SOUTH);

// Finally, add this new container to the JFrame's south position
        add(southContainer, BorderLayout.SOUTH);
    }



    // --- 4. CORE LOGIC METHODS ---

    private void loadShowtimes(String initialKey) {
        cmbShowtime.removeAllItems();

        // Only load the movie selected on the gallery screen
        cmbShowtime.addItem(initialKey);
        cmbShowtime.setSelectedIndex(0);
    }



    private void btnBackActionPerformed(ActionEvent evt) {
        // Close the current frame
        this.dispose();

        // Open the Movie Gallery frame
        MovieGalleryFrame gallery = new MovieGalleryFrame();
        gallery.setLocationRelativeTo(null);
        gallery.setVisible(true);
    }




    private void updateSeatMap() {
        String selectedKey = (String) cmbShowtime.getSelectedItem();
        if (selectedKey == null) {
            currentShowtime = null;
            pnlSeatLayout.removeAll();
            pnlSeatLayout.revalidate();
            pnlSeatLayout.repaint();
            return;
        }

        currentShowtime = BookingManager.getShowtime(selectedKey);
        pnlSeatLayout.removeAll();

        Seat[][] seats = currentShowtime.getSeats();
        int rows = seats.length;
        int cols = seats[0].length;
        pnlSeatLayout.setLayout(new GridLayout(rows, cols, 5, 5));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Seat seat = seats[r][c];
                JButton seatButton = new JButton(seat.getSeatId());
                customizeSeatButton(seatButton, seat);
                seatButton.addActionListener(e -> seatButtonActionPerformed(seat, seatButton));
                pnlSeatLayout.add(seatButton);
            }
        }
        pnlSeatLayout.revalidate();
        pnlSeatLayout.repaint();

        selectedSeats.clear();
        updateBillSummary();
    }

    private void customizeSeatButton(JButton button, Seat seat) {
        button.setEnabled(!seat.isBooked());
        button.setFont(new Font("Arial", Font.BOLD, 10));

        if (seat.isBooked()) {
            button.setBackground(new Color(105, 105, 105)); // Dim Gray (Booked)
            button.setForeground(Color.WHITE);
            button.setText("X");
        } else if (selectedSeats.contains(seat)) {
            button.setBackground(PRIMARY_BLUE.brighter()); // Brighter Blue (Selected)
            button.setForeground(Color.WHITE);
            button.setText(seat.getSeatId());
        } else {
            button.setBackground(AVAILABLE_SEAT_COLOR); // Light Gray (Available)
            button.setForeground(Color.BLACK);
            button.setText(seat.getSeatId());
        }
    }

    private void updateBillSummary() {
        // Ensure timer stops if it was running
        if (statusTimer.isRunning()) {
            statusTimer.stop();
            lblStatus.setForeground(Color.BLACK);
        }

        StringBuilder summary = new StringBuilder();
        double total = 0.0;

        if (currentShowtime == null) {
            summary.append("Select a movie to begin.");
            txtBillSummary.setText(summary.toString());
            lblTotal.setText("Total: Rs. 0.00");
            return;
        }

        summary.append(String.format("Movie: %s\n", currentShowtime.getMovie().getTitle()));
        summary.append(String.format("Time: %s\n", currentShowtime.getMovie().getTiming()));
        summary.append("-------------------------------------------\n");
        summary.append("Seats Selected:\n");

        if (selectedSeats.isEmpty()) {
            summary.append("  (No seats selected)\n");
        } else {
            for (Seat seat : selectedSeats) {
                double price = currentShowtime.getMovie().getPrice();
                summary.append(String.format("  - %s (Rs. %d)\n", seat.getSeatId(), (int)price));
                total += price;
            }
        }

        summary.append("-------------------------------------------\n");
        double gst = total * 0.18; // 18% GST (India standard)
        summary.append(String.format("Subtotal:  Rs. %.2f\n", total));
        summary.append(String.format("GST (18%%): Rs. %.2f\n", gst));
        summary.append("===========================================\n");
        summary.append(String.format("Total Payable: Rs. %.2f\n", total + gst));
        summary.append("===========================================\n");

        txtBillSummary.setText(summary.toString());
        lblTotal.setText(String.format("Total: Rs. %.2f", total + gst));
    }

    // --- 5. ACTION LISTENERS ---

    private void cmbShowtimeActionPerformed(ActionEvent evt) {
        String selectedKey = (String) cmbShowtime.getSelectedItem();
        if (selectedKey != null) {
            // Explicitly set the current showtime object
            currentShowtime = BookingManager.getShowtime(selectedKey);
        }
        selectedSeats.clear();
        updateSeatMap();
    }

    private void seatButtonActionPerformed(Seat seat, JButton button) {
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
            lblStatus.setText(seat.getSeatId() + " deselected.");
        } else {
            selectedSeats.add(seat);
            lblStatus.setText(seat.getSeatId() + " selected.");
        }
        customizeSeatButton(button, seat);
        updateBillSummary();
        pnlSeatLayout.revalidate();
        pnlSeatLayout.repaint();
    }

    private void btnClearCartActionPerformed(ActionEvent evt) {
        if (selectedSeats.isEmpty()) {
            lblStatus.setText("Cart is already empty.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear the entire cart?", "Confirm Clear", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            selectedSeats.clear();
            updateSeatMap();
            updateBillSummary();
            lblStatus.setText("Cart cleared successfully.");
        }
    }

    private void btnGeneratePdfBillActionPerformed(ActionEvent evt) {
        if (selectedSeats.isEmpty()) {
            lblStatus.setForeground(DANGER_RED);
            lblStatus.setText("Error: Please select seats before generating the bill.");
            statusTimer.start();
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm booking and generate PDF bill for " + selectedSeats.size() + " seats?",
                "Final Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        // Save the current bill content BEFORE booking (for the receipt)
        String finalBillContent = txtBillSummary.getText();

        // 1. Mark seats as permanently booked
        for(Seat seat : selectedSeats) {
            currentShowtime.bookSeat(seat.getSeatId());
        }

        // 2. Write bill to PDF file
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("Bill_%s.pdf", timestamp);

        boolean pdfSuccess = createPdfBill(filename, finalBillContent);

        if (pdfSuccess) {

            // --- Success UI Update ---
            lblStatus.setForeground(SECONDARY_GREEN);
            lblStatus.setText("SUCCESS: PDF Bill saved. Opening file now...");

            // *** NEW STEP: Open the PDF file ***
            boolean opened = openPdfFile(filename);

            if (!opened) {
                // Fallback status if OS failed to open the file
                lblStatus.setText("PDF saved, but could not be opened automatically.");
            }

        } else {
            // --- Failure UI Update ---
            JOptionPane.showMessageDialog(this,
                    "Error saving PDF bill. Seats have NOT been booked.",
                    "PDF Error",
                    JOptionPane.ERROR_MESSAGE);
            lblStatus.setForeground(DANGER_RED);
            lblStatus.setText("FAILURE: PDF creation failed. Seats have been rolled back.");

            // If PDF fails, roll back the seat booking
            for(Seat seat : selectedSeats) {
                seat.cancel();
            }
        }

        // 3. Display Final Receipt and reset state for NEW booking
        txtBillSummary.setText(finalBillContent +
                "\n\n--- TRANSACTION COMPLETE ---\nSeats are now booked. Clear cart to start new order.");

        selectedSeats.clear();
        updateSeatMap(); // Redraw map showing newly booked seats as 'X'
    }

    /**
     * Uses Apache PDFBox to create the PDF document.
     */
    private boolean createPdfBill(String filename, String content) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Standard PDF font (easy compilation)
            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                contentStream.setFont(font, 18f); // <-- FIX: Added 'f'
                contentStream.beginText();
                contentStream.setLeading(18f * 1.5f); // <-- FIX: Added 'f'
                contentStream.newLineAtOffset(50, 750);

                contentStream.showText("--- MOVIE TICKET RECEIPT ---");
                contentStream.newLine();
                contentStream.newLine();

                contentStream.setFont(font, 12f); // <-- FIX: Added 'f'

                // Write the detailed summary line by line
                for (String line : content.split("\n")) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }

                contentStream.newLine();
                contentStream.showText("THANK YOU for booking with us!");
                contentStream.endText();
            }

            document.save(filename);
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during PDF creation.", e);
            return false;
        }
    }

    /**
     * Opens the generated PDF file using the operating system's default viewer.
     */
    private boolean openPdfFile(String filename) {
        try {
            File pdfFile = new File(filename);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    // Check if the desktop environment supports the OPEN action
                    Desktop.getDesktop().open(pdfFile);
                    return true;
                } else {
                    logger.log(Level.WARNING, "Desktop API not supported, cannot open PDF automatically.");
                    return false;
                }
            } else {
                logger.log(Level.WARNING, "PDF file does not exist after creation attempt: " + filename);
                return false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not open PDF. Please check file permissions or install a PDF reader.",
                    "System Error",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.SEVERE, "Error opening PDF file.", e);
            return false;
        }
    }

    // --- 6. MAIN METHOD (For direct testing), deleted ---

}
