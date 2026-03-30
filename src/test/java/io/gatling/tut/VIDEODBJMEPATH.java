package io.gatling.tut;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import example.HelperClassTUT.*;

import java.time.Duration;

import static example.HelperClassTUT.VideoGameRequests.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;



public class VIDEODBJMEPATH extends  Simulation {

    ScenarioBuilder scn = scenario("Video Game Db - Clean Architecture")
            // 1st request: get all games
            .exec(
                    getAllGames()
                            .check(status().in(200, 201, 202)).check(jmesPath("[?id==`1`].name | [0]")
                    .is("Resident Evil 4")))
            .pause(5)

            // 2nd request: get game by id
            .exec(
                    getGameById(1)
                            .check(status().is(200)).check(bodyString().saveAs("responseBody"))
            )
            .pause(1, 10).exec(
                    session -> {
                        String responseBody = session.getString("responseBody");
                        System.out.println("Response Body: " + responseBody);
                        return session;
                    }
            )

            // 3rd request: get all games again
            .exec(
                    getAllGames()
                            .check(status().not(404),status().not( 500) , status().not(504))
                            .check(jmesPath("[1].id").saveAs("secondGameId"))

            )
            .pause(Duration.ofMillis(4000))
                .exec(
                        session-> {
                            System.out.println("Second game ID: " + session.getInt("secondGameId"));
                            return session;
                        } 
                );

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(HttpConfig.baseConfig());
    }


}
