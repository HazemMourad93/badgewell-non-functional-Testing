package io.gatling.tut;

import example.HelperClassTUT.HttpConfig;
import example.HelperClassTUT.VideoGameFlows;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import java.util.*;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;

public class CustomFeeder extends Simulation {

    private static final Iterator<Map<String, Object>> gameFeeder =
            Stream.generate(() -> {
                Map<String, Object> data = new HashMap<>();
                data.put("id", 100 + (int) (Math.random() * 1000));
                data.put("name", "Game-" + UUID.randomUUID().toString().substring(0, 8));
                data.put("releaseDate", "2025-01-01");
                data.put("reviewScore", 85);
                data.put("category", "Action");
                data.put("rating", "Universal");
                return data;
            }).iterator();

    ScenarioBuilder scn = scenario("Video Game Db - Basic Custom Feeder")
            .feed(gameFeeder)
            .exec(VideoGameFlows.getSpecificGameFlowJson());

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(HttpConfig.baseConfig());
    }



}
