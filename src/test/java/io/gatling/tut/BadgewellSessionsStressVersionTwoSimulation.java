package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsFlows;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;

public class BadgewellSessionsStressVersionTwoSimulation extends Simulation {

    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");

    ScenarioBuilder scn = scenario("Stress - Admin Learning Sessions")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(
                    40, 1, 10, "ASC", "9y9", admintoken1
            ));

    {
        setUp(
                scn.injectOpen(
                        stressPeakUsers(1000).during(60)
                )
        ).protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().gt(90.0),
                        global().failedRequests().percent().lt(10.0),
                        global().responseTime().percentile3().lt(4000),
                        global().responseTime().max().lt(10000),
                        details("Get learning sessions").successfulRequests().percent().gt(90.0)
                );
    }
}