package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsFlows;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.OpenInjectionStep.*;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;
import static io.gatling.javaapi.core.OpenInjectionStep.nothingFor;

public class BadgewellAdminPreSessionRushSimulation extends Simulation {

    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");


    private SetUp setUpScenario(ScenarioBuilder scenarioBuilder) {
        return setUp(
                scenarioBuilder.injectOpen(
                        nothingFor(Duration.ofSeconds(5)),
                        stressPeakUsers(480).during(180)
                )
        );
    }

    ScenarioBuilder scn = scenario("Spike - Admin Learning Sessions")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(
                    40, 1, 10, "ASC", "", admintoken1
            ));


    {
        setUpScenario(scn)
                .protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().gt(90.0),
                        global().failedRequests().percent().lt(10.0),
                        global().responseTime().percentile3().lt(5000),
                        global().responseTime().max().lt(12000),
                        details("Get learning sessions").successfulRequests().percent().gt(90.0)
                );
    }
}