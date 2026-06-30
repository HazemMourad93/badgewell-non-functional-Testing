package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsFlows;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.OpenInjectionStep.*;

public class BadgewellAdminLargePageSimulation extends Simulation {

    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");

    ScenarioBuilder scn = scenario("Real Life - Admin Large Page Review")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(
                    40, 1, 500, "ASC", "", admintoken1
            ));

    {
        setUp(
                scn.injectOpen(
                        rampUsers(20).during(Duration.ofSeconds(90)),
                        constantUsersPerSec(5).during(Duration.ofSeconds(30))
                )
        ).protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().gt(90.0),
                        global().failedRequests().percent().lt(10.0),
                        global().responseTime().percentile3().lt(4000),
                        global().responseTime().max().lt(9000),
                        details("Get learning sessions").successfulRequests().percent().gt(90.0)
                );
    }
}