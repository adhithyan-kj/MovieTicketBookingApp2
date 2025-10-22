package movieticketbookinggui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles all CRUD (Create, Read, Update, Delete) operations
 * between the application and the seat_status table.
 */
public class SeatDAO {
    private static final Logger logger = Logger.getLogger(SeatDAO.class.getName());
    private final Connection connection;

    public SeatDAO() {
        // The DAO object gets the existing connection instance from the manager
        this.connection = DBConnection.getConnection();
    }

    /**
     * Retrieves all permanently booked seats for a specific showtime from the DB.
     * This runs on application startup/movie selection to check permanent availability.
     * @param showtimeKey The unique identifier for the movie showing.
     * @return A Set of seat IDs that are booked.
     */
    public Set<String> getBookedSeats(String showtimeKey) {
        Set<String> bookedSeats = new HashSet<>();
        String sql = "SELECT seat_id FROM seat_status WHERE showtime_key = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, showtimeKey);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookedSeats.add(rs.getString("seat_id"));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving booked seats for key: " + showtimeKey, e);
        }
        return bookedSeats;
    }

    /**
     * Inserts a new booking record into the database.
     * This runs when the user clicks 'Download Bill' (final confirmation).
     * @param seatId The seat ID (e.g., A5).
     * @param showtimeKey The unique showtime identifier.
     * @return true if successful.
     */
    public boolean bookSeat(String seatId, String showtimeKey) {
        String sql = "INSERT INTO seat_status (seat_id, showtime_key) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, seatId);
            pstmt.setString(2, showtimeKey);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Seat " + seatId + " already booked or DB error.", e);
            // This usually fails if the seat is already booked (Primary Key constraint violation)
            return false;
        }
    }
}
