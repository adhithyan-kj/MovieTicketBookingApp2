package movieticketbookinggui;

/**
 * Represents a single seat. Removed serialization support.
 */
public class Seat {
    private final String seatId;
    private boolean isBooked;

    public Seat(String id) {
        this.seatId = id;
        this.isBooked = false;
    }

    // Encapsulation: Marks seat as booked (used for selectedSeats cart check)
    public boolean book() {
        if (!isBooked) {
            isBooked = true;
            return true;
        }
        return false;
    }

    public void cancel() {
        this.isBooked = false;
    }

    // NEW: Method used only during initial DB load to mark seat status
    // This allows the database logic to permanently mark a seat as taken
    // without running the interactive book() logic.
    public void forceBook() {
        this.isBooked = true;
    }

    // Getters
    public String getSeatId() {
        return seatId;
    }

    public boolean isBooked() {
        return isBooked;
    }

    @Override
    public String toString() {
        return seatId + (isBooked ? " (Booked)" : " (Available)");
    }
}
