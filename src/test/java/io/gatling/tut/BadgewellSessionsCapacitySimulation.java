package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsFlows;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.OpenInjectionStep.*;

public class BadgewellSessionsCapacitySimulation extends Simulation {

    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");

    ScenarioBuilder scn = scenario("Capacity - Admin Learning Sessions")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(
                    40, 1, 10, "ASC", "", admintoken1
            ));

    {
        setUp(
                scn.injectOpen(
                        rampUsers(20).during(Duration.ofSeconds(30)),
                        rampUsers(40).during(Duration.ofSeconds(30)),
                        rampUsers(60).during(Duration.ofSeconds(30)),
                        rampUsers(80).during(Duration.ofSeconds(30)),
                        rampUsers(100).during(Duration.ofSeconds(30))
                )
        ).protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().gt(90.0),
                        global().failedRequests().percent().lt(10.0),
                        global().responseTime().percentile3().lt(5000),
                        global().responseTime().max().lt(12000),
                        details("Get learning sessions").successfulRequests().percent().gt(90.0)
                );
    }
}