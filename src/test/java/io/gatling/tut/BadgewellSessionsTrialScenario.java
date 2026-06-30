package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsFlows;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;

public class BadgewellSessionsTrialScenario extends Simulation {

    public String admintoken1 = ConfigReader.properties.getProperty("admintoken1");

    private final ScenarioBuilder scn = scenario("My First Test")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(
                    40,
                    1,
                    10,
                    "ASC",
                    "9y9",
                    admintoken1
            ));

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(HttpConfig.baseConfig());
    }





}
