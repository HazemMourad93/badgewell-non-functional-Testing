//package io.gatling.tut;
//
//import example.HelperClassTUT.HttpConfig;
//import example.HelperClassTUT.VideoGameFlows;
//import io.gatling.javaapi.core.FeederBuilder;
//import io.gatling.javaapi.core.ScenarioBuilder;
//import io.gatling.javaapi.core.Simulation;  // ✅ Java API
//
//import static io.gatling.javaapi.core.CoreDsl.*;
//
//public class csv_simulation extends Simulation {
//
//    private static final FeederBuilder.FileBased<String> gameFeeder =
//            csv("Data.csv").circular();  // ✅ correct path
//
//    ScenarioBuilder scn = scenario("Video Game Db - CSV Feeder")
//            .feed(gameFeeder)
//            .exec(VideoGameFlows.getSpecificGameFlow());
//
//    {
//        setUp(
//                scn.injectOpen(atOnceUsers(1))
//        ).protocols(HttpConfig.baseConfig());
//    }
//}