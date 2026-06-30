package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsFlows;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.OpenInjectionStep.*;

public class BadgewellAdminSortToggleSimulation extends Simulation {

    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");

    ScenarioBuilder scn = scenario("Real Life - Admin Sort Toggle")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(40, 1, 10, "ASC", "", admintoken1))
            .pause(Duration.ofSeconds(2))
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(40, 1, 10, "DESC", "", admintoken1));

    {
        setUp(
                scn.injectOpen(
                        rampUsers(20).during(Duration.ofMinutes(2))
                )
        ).protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().gt(95.0),
                        global().failedRequests().percent().lt(5.0),
                        global().responseTime().percentile3().lt(3000),
                        global().responseTime().max().lt(7000),
                        details("Get learning sessions").successfulRequests().percent().gt(95.0)
                );
    }
}