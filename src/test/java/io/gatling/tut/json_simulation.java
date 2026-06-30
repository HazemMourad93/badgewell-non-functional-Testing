//package io.gatling.tut;
//
//import example.HelperClassTUT.HttpConfig;
//import example.HelperClassTUT.VideoGameFlows;
//import io.gatling.javaapi.core.FeederBuilder;
//import io.gatling.javaapi.core.ScenarioBuilder;
//import io.gatling.javaapi.core.Simulation;
//
//import static io.gatling.javaapi.core.CoreDsl.*;
//
//public class json_simulation extends Simulation {
//
//    private static final FeederBuilder.FileBased<Object> gameFeeder =
//            jsonFile("Data.json").circular();
//
//    ScenarioBuilder scn = scenario("Video Game Db - JSON Feeder")
//            .feed(gameFeeder)
//            .exec(VideoGameFlows.getSpecificGameFlowJson());
//
//    {
//        setUp(
//                scn.injectOpen(atOnceUsers(1))
//        ).protocols(HttpConfig.baseConfig());
//    }
//}
