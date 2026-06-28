package example.HelperClassTUT;


import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class VideoGameRequests {

    // Pure HTTP request function
    public static HttpRequestActionBuilder getAllGames() {
        return http("Get all video games")
                .get("/videogame");
    }

    public static HttpRequestActionBuilder getGameById(int id) {
        return http("Get video game by ID " + id)
                .get("/videogame/" + id);
    }

    public static HttpRequestActionBuilder getWithPause() {
        return http("Get all video games with pause")
                .get("/videogame");
    }

//    public static HttpRequestActionBuilder createNewGame(String token, VideoGameBody body) {
//        return http("Create New Game")
//                .post("api/videogames")
//                .header("Content-Type", "application/json")
//                .header("Authorization", "Bearer " + token)
//                .body(StringBody(body.toJson())) // 🔥 use POJO → JSON
//                .asJson();
//    }


    public static HttpRequestActionBuilder createNewGame(String token, VideoGameBody body) {
        return http("Create New Game - #{gameName}")
                .post("/videogame")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(StringBody(session -> new VideoGameBody(
                        session.getString("gameName"),
                        session.getString("releaseDate"),
                        session.getInt("reviewScore"),
                        session.getString("category"),
                        session.getString("rating")
                ).toJson()));
    }

    // ✅ Feeder-based - matches video: GET by gameId, check gameName
    public static HttpRequestActionBuilder getSpecificGame() {
        return http("Get video game with name - #{gameName}")
                .get("/videogame/#{gameId}")
                .check(jmesPath("name").isEL("#{gameName}"));
    }

    public static HttpRequestActionBuilder getSpecificGameName() {
        return http("Get video game with name - #{name}")
                .get("/videogame/#{id}")
                .check(jmesPath("name").isEL("#{name}"));
    }


    // ✅ Custom Iterator Feeder request - GET by gameId
    public static HttpRequestActionBuilder getGameByCustomFeeder() {
        return http("Get video game with id - #{gameId}")
                .get("/videogame/#{gameId}");
    }

    // ── new request ────────────────────────────────────────────────────────────
    /**
     * Reads #{gameId} from the Gatling session (populated by the custom feeder).
     */
    public static HttpRequestActionBuilder getVideoGame() {
        return http("Get video game with id - #{gameId}")
                .get("/videogame/#{gameId}");
    }






}
