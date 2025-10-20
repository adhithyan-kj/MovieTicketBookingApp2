package movieticketbookinggui;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLaf; // <--- Correct import for LookAndFeel object
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Font; // Ensure Font is imported for the put call

/**
 * Main application class to launch the GUI frame with FlatLaf MacLight Theme.
 */
public class MainApp {
    private static final Logger logger = Logger.getLogger(MainApp.class.getName());

    public static void main(String[] args) {

        // --- Set FlatLaf MacLight Theme ---
        try {
            // This is the correct way to set the LookAndFeel object
            UIManager.setLookAndFeel(new com.formdev.flatlaf.themes.FlatMacLightLaf());

            // Optional: Customize the title bar font after setting the L&F
            UIManager.put("TitlePane.font", UIManager.getFont("TitlePane.font").deriveFont(Font.BOLD, 14f));

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to initialize FlatLaf theme.", ex);
        }

        // Launch the GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {

            BookingFrame frame = new BookingFrame();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}