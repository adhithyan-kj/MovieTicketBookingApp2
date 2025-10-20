package movieticketbookinggui;

public class Seat {
    private final String seatId;
    private boolean isBooked;

    public Seat(String row, int number) {
        this.seatId = row + number;
        this.isBooked = false;
    }

    // Encapsulation: Check status before booking
    public boolean book() {
        if (!isBooked) {
            isBooked = true;
            return true;
        }
        return false;
    }

    public void cancel() {
        isBooked = false;
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