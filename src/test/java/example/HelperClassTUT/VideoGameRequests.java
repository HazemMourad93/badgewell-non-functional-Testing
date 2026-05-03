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

    public static HttpRequestActionBuilder createNewGame(String token, VideoGameBody body) {
        return http("Create New Game")
                .post("api/videogames")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(StringBody(body.toJson())) // 🔥 use POJO → JSON
                .asJson();
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







}
