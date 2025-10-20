package movieticketbookinggui;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages all movie data, showtimes, and rich details.
 */
public class BookingManager {
    private static final Map<String, Showtime> SHOWTIMES = new HashMap<>();
    private static final Map<String, MovieDetail> MOVIE_DETAILS = new HashMap<>();

    static {
        // --- 1. DEFINE MOVIES ---
        Movie m1 = new Movie("The Pet Detective", "10:00 AM", 120.00);
        Movie m2 = new Movie("Kantara", "1:30 PM", 150.00);
        Movie m3 = new Movie("Lokah", "7:00 PM", 120.00);

        // --- 2. POPULATE SHOWTIMES (Seating Data) ---
        SHOWTIMES.put(m1.toString(), new Showtime(m1, 5, 10));
        SHOWTIMES.put(m2.toString(), new Showtime(m2, 5, 10));
        SHOWTIMES.put(m3.toString(), new Showtime(m3, 5, 10));

        // --- 3. POPULATE RICH MOVIE DETAILS (The new Menu Data) ---

        // Details for Pet Detective
        MOVIE_DETAILS.put(m1.getTitle(), new MovieDetail(
                "Attempting to impress his childhood sweetheart, an underachieving detective takes on a missing pet case that soon spirals into a chaotic, high-stakes conspiracy involving smugglers and kidnappings",
                "/movieticketbookinggui/assets/the_pet_detective_poster.jpg",
                Map.of("Yash", "/movieticketbookinggui/assets/actor1.jpg",
                        "Sharaf U Dheen, Anupama Parameswaran, Shyam Mohan, Bhagath Manuel, Vinay Forrt, Vijayaraghavan, Vinayakan, Joemon Jyothir, Renji Panicker, Shobi Thilakan, P. P. Kunhikrishnan, Jai Vishnu, Praseetha Menon, Sanju Madhu, and Jagadeesh Kumar.", "/movieticketbookinggui/assets/actor2.jpg")
        ));

        // Details for Kantara
        MOVIE_DETAILS.put(m2.getTitle(), new MovieDetail(
                "A visually stunning action-thriller rooted in local folklore and mythology.",
                "/movieticketbookinggui/assets/kantara_poster.jpg",
                Map.of("Rishab Shetty, Sapthami Gowda, Kishore, Achyuth Kumar, Pramod Shetty, Prakash Thuminad, Manasi Sudhir, Naveen D. Padil", "/movieticketbookinggui/assets/actor3.jpg")
        ));

        // Details for Lokah
        MOVIE_DETAILS.put(m3.getTitle(), new MovieDetail(
                "Chandra, a young woman, arrives in Bengaluru with a mission. Her neighbor, Sunny, sets out to unravel the mystery about her and they are on an unexpected adventure ride together.",
                "/movieticketbookinggui/assets/lokah_poster.jpg",
                Map.of("Kalyani Priyadarshan, Naslen, Sandy Master, Arun Kurian, Chandu Salimkumar, Raghunath Paleri, Vijayaraghavan, Jain Andrews, Shivajith Padmanabhan, Sarath Sabha, Nishanth Sagar, Sam Mohan, Sunny Wayne, Tovino Thomas, Dulquer Salmaan, Mammootty (voice over), Durga C. Vinod", "/movieticketbookinggui/assets/actor4.jpg")
        ));
    }

    public static Map<String, Showtime> getAllShowtimes() {
        return SHOWTIMES;
    }

    public static Showtime getShowtime(String key) {
        return SHOWTIMES.get(key);
    }

    // NEW: Getter for the initial menu screen
    public static Map<String, MovieDetail> getAllMovieDetails() {
        return MOVIE_DETAILS;
    }

    // NEW: Getter for a specific showtime key based on movie title
    public static String getShowtimeKeyByTitle(String title) {
        // Simple search for the first showtime matching the title
        for (String key : SHOWTIMES.keySet()) {
            if (key.startsWith(title)) {
                return key;
            }
        }
        return null;
    }
}