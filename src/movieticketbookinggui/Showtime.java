package movieticketbookinggui;

import java.util.Set;

/**
 * Manages the seating arrangement for a specific movie, now integrated with DB.
 * It retrieves seat status from the database upon initialization.
 */
public class Showtime {
    private final Movie movie;
    private final Seat[][] seats;
    private final String showtimeKey;

    public Showtime(Movie movie, int rows, int cols) {
        this.movie = movie;
        // The unique identifier for this showing is the combination of movie title and time.
        this.showtimeKey = movie.toString();
        this.seats = new Seat[rows][cols];

        // Initialize seats and check the database immediately
        SeatDAO dao = new SeatDAO();
        initializeSeats(rows, cols, dao);
    }

    private void initializeSeats(int rows, int cols, SeatDAO dao) {
        // --- 1. Get permanently booked seats from DB ---
        Set<String> dbBookedSeats = dao.getBookedSeats(this.showtimeKey);

        // --- 2. Initialize and mark seats based on DB status ---
        char rowChar = 'A';
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String seatId = String.valueOf(rowChar) + (j + 1);
                Seat seat = new Seat(seatId);

                // If the seat is found in the database set, mark it permanently booked
                if (dbBookedSeats.contains(seatId)) {
                    seat.forceBook();
                }
                seats[i][j] = seat;
            }
            rowChar++;
        }
    }

    /**
     * Attempts to book a seat by first committing the change to the database.
     * @param seatId The seat to book.
     * @return true if the seat was successfully booked in the DB and locally.
     */
    public boolean bookSeat(String seatId) {
        for (Seat[] row : seats) {
            for (Seat seat : row) {
                if (seat.getSeatId().equalsIgnoreCase(seatId)) {
                    // Try to book seat in the DB first via the DAO
                    SeatDAO dao = new SeatDAO();
                    if (dao.bookSeat(seatId, this.showtimeKey)) {
                        // If DB commit succeeds, update local object status
                        return seat.book();
                    }
                    return false; // DB booking failed (e.g., seat already booked by another user)
                }
            }
        }
        return false; // Seat not found
    }

    // --- Other Getters ---
    public Movie getMovie() { return movie; }
    public Seat[][] getSeats() { return seats; }
}
