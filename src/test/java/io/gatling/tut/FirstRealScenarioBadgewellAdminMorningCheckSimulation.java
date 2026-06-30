package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsFlows;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;

public class FirstRealScenarioBadgewellAdminMorningCheckSimulation extends Simulation {

    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");

    ScenarioBuilder scn = scenario("Real Life - Admin Morning Check")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(
                    40, 1, 10, "ASC", "", admintoken1
            ));

    {
        setUp(
                scn.injectOpen(
                        rampUsers(15).during(Duration.ofMinutes(2)),
                        constantUsersPerSec(5).during(Duration.ofMinutes(5))
                )
        ).protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().gt(95.0),
                        global().failedRequests().percent().lt(5.0),
                        global().responseTime().percentile3().lt(2500),
                        global().responseTime().max().lt(6000),
                        details("Get learning sessions").successfulRequests().percent().gt(95.0)
                );
    }
}