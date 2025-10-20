package movieticketbookinggui;

import java.util.ArrayList;
import java.util.List;

public class Showtime {
    private final Movie movie;
    private final Seat[][] seats; // 2D array for theater layout

    public Showtime(Movie movie, int rows, int cols) {
        this.movie = movie;
        this.seats = new Seat[rows][cols];
        initializeSeats(rows, cols);
    }

    private void initializeSeats(int rows, int cols) {
        char rowChar = 'A';
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Create seats like A1, A2, B1, B2...
                seats[i][j] = new Seat(String.valueOf(rowChar), j + 1);
            }
            rowChar++;
        }
    }

    // Method to book a seat
    public boolean bookSeat(String seatId) {
        for (Seat[] row : seats) {
            for (Seat seat : row) {
                if (seat.getSeatId().equalsIgnoreCase(seatId)) {
                    return seat.book(); // Use the Seat object's booking logic
                }
            }
        }
        return false; // Seat not found
    }

    // Getters
    public Movie getMovie() {
        return movie;
    }

    public Seat[][] getSeats() {
        return seats;
    }
    
    public int getTotalSeats() {
        return seats.length * seats[0].length;
    }
    
    public List<Seat> getAvailableSeats() {
        List<Seat> available = new ArrayList<>();
        for (Seat[] row : seats) {
            for (Seat seat : row) {
                if (!seat.isBooked()) {
                    available.add(seat);
                }
            }
        }
        return available;
    }
}