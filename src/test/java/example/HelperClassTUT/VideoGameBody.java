package example.HelperClassTUT;

import org.json.simple.JSONObject;

public class VideoGameBody {

    private final String name;
    private final String releaseDate;
    private final int reviewScore;
    private final String category;
    private final String rating;

    public VideoGameBody(String name, String releaseDate, int reviewScore, String category, String rating) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.reviewScore = reviewScore;
        this.category = category;
        this.rating = rating;
    }


    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("releaseDate", releaseDate);
        json.put("reviewScore", reviewScore);
        json.put("category", category);
        json.put("rating", rating);
        return json.toString();

    }
}
