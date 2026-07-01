package io.gatling.tut;

import example.BadgewellAPIs.LearningSessionsRequests;
import example.HelperClassTUT.ConfigReader;
import example.HelperClassTUT.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ThirdRealBadgewellAdminPaginationSimulation extends Simulation {

    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");

    ScenarioBuilder scn = scenario("Real Life - Admin Pagination Review");

    {
        for (int page = 1; page <= 10; page++) {
            scn = scn.exec(
                    LearningSessionsRequests.getLearningSessions(
                            40,
                            page,
                            50,
                            "ASC",
                            "EXPIRED-CO",
                            admintoken1
                    ).check(status().is(200))
            ).pause(Duration.ofSeconds(5));
        }
    }

    {
        setUp(
                scn.injectOpen(
                        rampUsers(10).during(Duration.ofMinutes(2))
                )
        ).protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().gt(95.0),
                        global().failedRequests().percent().lt(5.0),
                        global().responseTime().percentile3().lt(30000),
                        global().responseTime().max().lt(70000),
                        details("Get learning sessions").successfulRequests().percent().gt(95.0)
                );
    }
}