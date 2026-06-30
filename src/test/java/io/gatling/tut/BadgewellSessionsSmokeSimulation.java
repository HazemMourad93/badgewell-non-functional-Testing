package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsFlows;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;

public class BadgewellSessionsSmokeSimulation extends Simulation {

    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");

    ScenarioBuilder scn = scenario("Smoke - Admin Learning Sessions")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(
                    40, 1, 10, "ASC", "9y9", admintoken1
            ));

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().is(100.0),
                        global().failedRequests().count().is(0L),
                        global().responseTime().max().lt(5000),
                        details("Get learning sessions").successfulRequests().percent().is(100.0)
                );
    }
}