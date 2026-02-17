package example.HelperClassTUT;


import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.core.CoreDsl.exec;

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



}
