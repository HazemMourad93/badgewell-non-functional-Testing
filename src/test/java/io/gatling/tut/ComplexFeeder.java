//package io.gatling.tut;
//
//import example.HelperClassTUT.ConfigReader;
//import example.HelperClassTUT.HttpConfig;
//import example.HelperClassTUT.VideoGameFeeder;
//import example.HelperClassTUT.VideoGameFlows;
//import io.gatling.javaapi.core.CoreDsl;
//import io.gatling.javaapi.core.ScenarioBuilder;
//import io.gatling.javaapi.core.Simulation;
//
//import static io.gatling.javaapi.core.CoreDsl.*;
//
//public class ComplexFeeder extends Simulation {
//
//    public String token = ConfigReader.properties.getProperty("demoToken");
//
//    ScenarioBuilder scn = CoreDsl.scenario("Video Game Performance Test")
//            .repeat(10).on(
//                    feed(VideoGameFeeder.customFeeder())
//                            .exec(session -> {
//                                return session.set("body", VideoGameFeeder.bodyFromSession(
//                                        new java.util.HashMap<>() {{
//                                            put("gameName",    session.getString("gameName"));
//                                            put("releaseDate", session.getString("releaseDate"));
//                                            put("reviewScore", session.getInt("reviewScore"));
//                                            put("category",    session.getString("category"));
//                                            put("rating",      session.getString("rating"));
//                                        }}
//                                ));
//                            })
//                            .exec(session -> {
//                                example.HelperClassTUT.VideoGameBody body =
//                                        (example.HelperClassTUT.VideoGameBody) session.get("body");
//                                return session;
//                            })
//                            .exec(VideoGameFlows.createNewGameFlow(token,
//                                    new example.HelperClassTUT.VideoGameBody(
//                                            "#{gameName}",
//                                            "#{releaseDate}",
//                                            0,
//                                            "#{category}",
//                                            "#{rating}"
//                                    )
//                            ))
//                            .pause(1)
//            );
//
//    {
//        setUp(
//                scn.injectOpen(CoreDsl.atOnceUsers(1))
//        ).protocols(HttpConfig.baseConfig());
//    }
//}