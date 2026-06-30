package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsFlows;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.OpenInjectionStep.*;

public class BadgewellSessionsLoadSimulation extends Simulation {

    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");

    ScenarioBuilder scn = scenario("Load - Admin Learning Sessions")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(
                    40, 1, 10, "ASC", "9y9", admintoken1
            ));

    {
        setUp(
                scn.injectOpen(
                        rampUsers(50).during(Duration.ofSeconds(30)),
                        constantUsersPerSec(10).during(Duration.ofMinutes(5))
                )
        ).protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().gt(95.0),
                        global().failedRequests().percent().lt(5.0),
                        global().responseTime().percentile3().lt(20000),
                        global().responseTime().max().lt(50000),
                        details("Get learning sessions").successfulRequests().percent().gt(95.0)
                );
    }
}