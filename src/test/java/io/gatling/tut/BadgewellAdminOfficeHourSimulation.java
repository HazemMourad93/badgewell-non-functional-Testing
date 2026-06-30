package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsFlows;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.OpenInjectionStep.*;

public class BadgewellAdminOfficeHourSimulation extends Simulation {

    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");

    ScenarioBuilder defaultList = scenario("Admin Default List")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(40, 1, 10, "ASC", "", admintoken1));

    ScenarioBuilder searchFlow = scenario("Admin Search Flow")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(40, 1, 10, "ASC", "onboarding", admintoken1));

    ScenarioBuilder paginationFlow = scenario("Admin Pagination Flow")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(40, 2, 10, "ASC", "", admintoken1));

    ScenarioBuilder sortFlow = scenario("Admin Sort Flow")
            .exec(LearningSessionsFlows.getLearningSessionsBasicFlow(40, 1, 10, "DESC", "", admintoken1));

    {
        setUp(
                defaultList.injectOpen(rampUsers(30).during(Duration.ofMinutes(3))),
                searchFlow.injectOpen(rampUsers(15).during(Duration.ofMinutes(3))),
                paginationFlow.injectOpen(rampUsers(9).during(Duration.ofMinutes(3))),
                sortFlow.injectOpen(rampUsers(6).during(Duration.ofMinutes(3)))
        ).protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().gt(95.0),
                        global().failedRequests().percent().lt(5.0),
                        global().responseTime().percentile3().lt(3500),
                        global().responseTime().max().lt(8000),
                        details("Get learning sessions").successfulRequests().percent().gt(95.0)
                );
    }
}