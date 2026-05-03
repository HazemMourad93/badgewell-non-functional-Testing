package io.gatling.tut;

import example.HelperClassTUT.HttpConfig;
import example.HelperClassTUT.VideoGameFlows;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;

public class CustomFeeder extends Simulation {

    private static final Iterator<Map<String, Object>> customFeeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                Random rand = new Random();
                int gameId = rand.nextInt(10) + 1;
                return Collections.singletonMap("gameId", gameId);
            }).iterator();

    ScenarioBuilder scn = scenario("Video Game Db - Custom Feeder")
            .feed(customFeeder)
            .exec(VideoGameFlows.getGameByCustomFeederFlow());

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(HttpConfig.baseConfig());
    }


}
