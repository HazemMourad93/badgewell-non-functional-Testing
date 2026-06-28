//package io.gatling.tut;
//
//import example.HelperClassTUT.*;
//import io.gatling.javaapi.core.CoreDsl;
//import io.gatling.javaapi.core.ScenarioBuilder;
//import io.gatling.javaapi.core.Simulation;
//import io.restassured.RestAssured;
//import io.restassured.config.ObjectMapperConfig;
//import io.restassured.config.RestAssuredConfig;
//import io.restassured.mapper.ObjectMapperType;
//
//import static io.gatling.core.Predef.scenario;
//import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;
//
//public class CreateAGame extends Simulation {
//
//
//    public String token = ConfigReader.properties.getProperty("demoToken");
//
//    ScenarioBuilder scn = CoreDsl.scenario("Video Game Performance Test")
//            .repeat(10).on(
//                    CoreDsl.exec(VideoGameFlows.createNewGameFlow(token))
//                            .pause(1)
//            );
//
//    {
//        setUp(
//                scn.injectOpen(CoreDsl.atOnceUsers(1))
//        ).protocols(HttpConfig.baseConfig());
//    }
//
//
//
//
//}
