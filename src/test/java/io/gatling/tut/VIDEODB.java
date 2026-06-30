//package io.gatling.tut;
//
//import io.gatling.javaapi.core.Simulation;
//import io.gatling.javaapi.core.*;
//import io.gatling.javaapi.http.*;
//import example.HelperClassTUT.*;
//
//import java.time.Duration;
//
//import static io.gatling.javaapi.core.CoreDsl.*;
//import static io.gatling.javaapi.http.HttpDsl.*;
//
//
//public class VIDEODB extends Simulation {
//
//    ScenarioBuilder scn = scenario("Video Game Db - Clean Architecture")
//            .exec(VideoGameRequests.getAllGames())
//            .pause(5)
//            .exec(VideoGameRequests.getGameById(1))
//            .pause(1, 10)
//            .exec(VideoGameRequests.getAllGames())
//            .pause(Duration.ofMillis(4000));
//
//    {
//        setUp(
//                scn.injectOpen(
//
//                        nothingFor(1), // 1
//                        atOnceUsers(10), // 2
//                        rampUsers(10).during(5)
//
//                )
//        ).protocols(HttpConfig.baseConfig());
//    }
//
//}
