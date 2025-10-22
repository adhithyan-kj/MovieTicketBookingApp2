package movieticketbookinggui;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages all movie data, showtimes, and rich details.
 * Data persistence for seat status is delegated to SeatDAO.
 */
public class BookingManager {

    // Static data that defines the structure and movie catalog
    private static final Map<String, Showtime> SHOWTIMES = new HashMap<>();
    private static final Map<String, MovieDetail> MOVIE_DETAILS = new HashMap<>();

    static {
        // --- This data remains static, but seat status is handled by the DB via Showtime ---

        // 1. DEFINE MOVIES
        Movie m1 = new Movie("The Pet Detective", "10:00 AM", 120.00);
        Movie m2 = new Movie("Kantara", "1:30 PM", 150.00);
        Movie m3 = new Movie("Lokah", "7:00 PM", 120.00);

        // 2. POPULATE SHOWTIMES (Seating Data)
        // Showtime constructor automatically triggers DB check now.
        SHOWTIMES.put(m1.toString(), new Showtime(m1, 5, 10));
        SHOWTIMES.put(m2.toString(), new Showtime(m2, 5, 10));
        SHOWTIMES.put(m3.toString(), new Showtime(m3, 5, 10));

        // --- 3. POPULATE RICH MOVIE DETAILS ---

        // Details for The Pet Detective
        MOVIE_DETAILS.put(m1.getTitle(), new MovieDetail(
                "Attempting to impress his childhood sweetheart, an underachieving detective takes on a missing pet case that soon spirals into a chaotic, high-stakes conspiracy involving smugglers and kidnappings.",
                "/movieticketbookinggui/assets/the_pet_detective_poster.jpg",
                // Corrected Map structure: Actor Name -> Photo Path
                Map.of("Yash", "/movieticketbookinggui/assets/actor1.jpg",
                        "Sharaf U Dheen", "/movieticketbookinggui/assets/actor2.jpg",
                        "Anupama Parameswaran", "/movieticketbookinggui/assets/actor2.jpg",
                        "Vinay Forrt", "/movieticketbookinggui/assets/actor2.jpg",
                        "Vijayaraghavan", "/movieticketbookinggui/assets/actor2.jpg")
        ));

        // Details for Kantara
        MOVIE_DETAILS.put(m2.getTitle(), new MovieDetail(
                "A visually stunning action-thriller rooted in local folklore and mythology.",
                "/movieticketbookinggui/assets/kantara_poster.jpg",
                Map.of("Rishab Shetty", "/movieticketbookinggui/assets/actor3.jpg",
                        "Sapthami Gowda", "/movieticketbookinggui/assets/actor3.jpg",
                        "Kishore", "/movieticketbookinggui/assets/actor3.jpg",
                        "Achyuth Kumar", "/movieticketbookinggui/assets/actor3.jpg")
        ));

        // Details for Lokah
        MOVIE_DETAILS.put(m3.getTitle(), new MovieDetail(
                "Chandra, a young woman, arrives in Bengaluru with a mission. Her neighbor, Sunny, sets out to unravel the mystery about her and they are on an unexpected adventure ride together.",
                "/movieticketbookinggui/assets/lokah_poster.jpg",
                Map.of("Kalyani Priyadarshan", "/movieticketbookinggui/assets/actor4.jpg",
                        "Naslen", "/movieticketbookinggui/assets/actor4.jpg",
                        "Sandy Master", "/movieticketbookinggui/assets/actor4.jpg",
                        "Arun Kurian", "/movieticketbookinggui/assets/actor4.jpg",
                        "Tovino Thomas", "/movieticketbookinggui/assets/actor4.jpg")
        ));
    }

    // --- ACCESSOR METHODS ---
    public static Map<String, Showtime> getAllShowtimes() {
        return SHOWTIMES;
    }

    public static Showtime getShowtime(String key) {
        return SHOWTIMES.get(key);
    }

    public static Map<String, MovieDetail> getAllMovieDetails() {
        return MOVIE_DETAILS;
    }

    public static String getShowtimeKeyByTitle(String title) {
        for (String key : SHOWTIMES.keySet()) {
            if (key.startsWith(title)) {
                return key;
            }
        }
        return null;
    }
}
