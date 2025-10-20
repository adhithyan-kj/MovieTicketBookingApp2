package movieticketbookinggui;

/**
 * Represents the details of a movie, now including price.
 */
public class Movie {
    private final String title;
    private final String timing;
    private final double price; // <-- NEW: Price per ticket

    public Movie(String title, String timing, double price) {
        this.title = title;
        this.timing = timing;
        this.price = price;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getTiming() {
        return timing;
    }

    public double getPrice() { // <-- NEW GETTER
        return price;
    }

    @Override
    public String toString() {
        return title + " - " + timing + " (Rs. " + (int)price + ")";
    }
}