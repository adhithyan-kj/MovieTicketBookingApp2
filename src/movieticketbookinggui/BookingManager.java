package movieticketbookinggui;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the collection of all available Showtimes.
 */
public class BookingManager {
    // Map to hold all showtimes, key is a unique Movie Title - Time string
    private static final Map<String, Showtime> SHOWTIMES = new HashMap<>();

    static {
        // --- Initialize sample data with prices ---
        // Price added as the third constructor argument
        Movie m1 = new Movie("Kantara", "10:00 AM", 120.00);
        Movie m2 = new Movie("The Pet Detective", "1:30 PM", 150.00);
        Movie m3 = new Movie("Lokah: Chapter 1 - Chandra", "7:00 PM", 120.00);

        // Create 5 rows (A-E), 10 columns (1-10) theater for each
        SHOWTIMES.put(m1.toString(), new Showtime(m1, 5, 10));
        SHOWTIMES.put(m2.toString(), new Showtime(m2, 5, 10));
        SHOWTIMES.put(m3.toString(), new Showtime(m3, 5, 10));

        // Pre-book a few seats for testing availability display
        SHOWTIMES.get(m1.toString()).bookSeat("C3");
        SHOWTIMES.get(m1.toString()).bookSeat("C4");
        SHOWTIMES.get(m1.toString()).bookSeat("E5");

        SHOWTIMES.get(m2.toString()).bookSeat("C6");
        SHOWTIMES.get(m2.toString()).bookSeat("C7");
    }

    public static Map<String, Showtime> getAllShowtimes() {
        return SHOWTIMES;
    }

    public static Showtime getShowtime(String key) {
        return SHOWTIMES.get(key);
    }
}