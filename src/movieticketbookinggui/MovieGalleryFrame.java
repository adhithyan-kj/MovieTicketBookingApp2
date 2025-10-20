package movieticketbookinggui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Map;

/**
 * Initial frame showing movie posters and details (like a modern menu).
 */
public class MovieGalleryFrame extends JFrame {
    private final String FRAME_TITLE = "Movie Ticket Booking - Select Movie";

    public MovieGalleryFrame() {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);

        // Use a scrollable panel to hold the movie cards
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        mainPanel.setBackground(Color.WHITE);

        loadMovieGallery(mainPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);
    }

    private void loadMovieGallery(JPanel container) {
        Map<String, MovieDetail> movieDetails = BookingManager.getAllMovieDetails();

        for (Map.Entry<String, MovieDetail> entry : movieDetails.entrySet()) {
            String title = entry.getKey();
            MovieDetail detail = entry.getValue();

            // Create a Card for each movie
            JPanel movieCard = createMovieCard(title, detail);
            container.add(movieCard);
        }
    }

    private JPanel createMovieCard(String title, MovieDetail detail) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setPreferredSize(new Dimension(250, 400));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        card.setBackground(Color.WHITE);

        // 1. POSTER (Center)
        JLabel posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(250, 300));

        URL imageURL = getClass().getResource(detail.posterPath);
        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            Image img = icon.getImage().getScaledInstance(250, 300, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(img));
        } else {
            posterLabel.setText("No Poster Found");
            posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        card.add(posterLabel, BorderLayout.CENTER);

        // 2. INFO PANEL (South)
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        infoPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(titleLabel);

        JButton bookButton = new JButton("Book Now");
        bookButton.setBackground(new Color(52, 168, 83)); // Green
        bookButton.setForeground(Color.WHITE);

        // Action: Open the detailed booking screen
        bookButton.addActionListener(e -> showBookingScreen(title));

        // Action: Display details on movie name click
        titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        titleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showMovieDetailsDialog(title, detail);
            }
        });

        card.add(infoPanel, BorderLayout.NORTH);
        card.add(bookButton, BorderLayout.SOUTH);

        return card;
    }

    private void showBookingScreen(String title) {
        String showtimeKey = BookingManager.getShowtimeKeyByTitle(title);
        if (showtimeKey != null) {
            // Close gallery and open booking frame for the selected movie
            this.dispose();
            new BookingFrame(showtimeKey).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No showtimes found for " + title, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMovieDetailsDialog(String title, MovieDetail detail) {
        // Create a detailed JDialog to show description and cast
        JDialog dialog = new JDialog(this, "Details: " + title, true); // Modal dialog
        dialog.setSize(600, 500);
        dialog.setLayout(new BorderLayout(10, 10));

        // Description
        JTextArea descriptionArea = new JTextArea(detail.description);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createTitledBorder("Synopsis"));
        dialog.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        // Cast Panel
        JPanel castPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        castPanel.setBorder(BorderFactory.createTitledBorder("Cast"));

        for (Map.Entry<String, String> castEntry : detail.castMap.entrySet()) {
            JPanel actorCard = new JPanel(new BorderLayout(5, 5));

            // Actor Photo
            JLabel photoLabel = new JLabel(castEntry.getKey(), SwingConstants.CENTER);
            URL imageURL = getClass().getResource(castEntry.getValue());
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image img = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
                photoLabel.setIcon(new ImageIcon(img));
                photoLabel.setText(castEntry.getKey());
                photoLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
                photoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            } else {
                photoLabel.setText(castEntry.getKey());
            }
            actorCard.add(photoLabel, BorderLayout.CENTER);
            castPanel.add(actorCard);
        }

        dialog.add(castPanel, BorderLayout.NORTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}