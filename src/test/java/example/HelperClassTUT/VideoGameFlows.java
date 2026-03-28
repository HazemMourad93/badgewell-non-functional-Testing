package example.HelperClassTUT;

import io.gatling.javaapi.core.ChainBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VideoGameFlows {

    // 1️⃣ Get all games with validation
    public static io.gatling.javaapi.core.ChainBuilder getAllGamesFlow() {
        return exec(
                VideoGameRequests.getAllGames()
                        .check(status().in(200, 201, 202))
                        .check(jsonPath("$[?(@.id==1)].name").is("Resident Evil 4"))
        ).pause(5);
    }

    // 2️⃣ Get game by ID
    public static io.gatling.javaapi.core.ChainBuilder getGameByIdFlow(int id) {
        return exec(
                VideoGameRequests.getGameById(id)
                        .check(status().is(200))
        ).pause(1, 10);
    }

    // 3️⃣ Get all games again with basic checks
    public static ChainBuilder getAllGamesBasicFlow() {
        return exec(
                VideoGameRequests.getWithPause()
                        .check(
                                status().not(404),
                                status().not(500),
                                status().not(504)
                        )
        ).pause(Duration.ofMillis(4000));
    }


// FIXED LOOP

    public static ChainBuilder getAllGamesBasicFlowLooped(int times) {
        return repeat(times).on(
                exec(
                        VideoGameRequests.getWithPause()
                                .check(
                                        status().not(404),
                                        status().not(500),
                                        status().not(504)
                                )
                ).pause(Duration.ofMillis(4000))
        );
    }


    public static ChainBuilder getAllGamesConditionalLoop() {
        return asLongAs(session -> session.getInt("counter") < 5).on(
                exec(
                        VideoGameRequests.getWithPause()
                                .check(
                                        status().not(404),
                                        status().not(500),
                                        status().not(504)
                                )
                )
                        .exec(session -> session.set("counter", session.getInt("counter") + 1))
                        .pause(Duration.ofMillis(4000))
        );
    }

    public static ChainBuilder getAllGamesBasicFlowDuring(int durationSeconds) {
        return during(durationSeconds).on(
                exec(
                        VideoGameRequests.getWithPause()
                                .check(
                                        status().not(404),
                                        status().not(500),
                                        status().not(504)
                                )
                ).pause(Duration.ofMillis(4000))
        );
    }



}
