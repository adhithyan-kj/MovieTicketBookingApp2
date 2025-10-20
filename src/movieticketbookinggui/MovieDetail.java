package movieticketbookinggui;

import java.util.Map;

/**
 * Stores rich data like description, cast, and asset paths for a movie.
 */
public class MovieDetail {
    public final String description;
    public final String posterPath;
    public final Map<String, String> castMap; // Actor Name -> Photo Path

    public MovieDetail(String description, String posterPath, Map<String, String> castMap) {
        this.description = description;
        this.posterPath = posterPath;
        this.castMap = castMap;
    }
}