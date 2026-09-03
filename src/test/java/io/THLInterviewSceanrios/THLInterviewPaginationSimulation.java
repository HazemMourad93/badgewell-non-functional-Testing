package io.THLInterviewSceanrios;

import example.HelperClassTUT.ConfigReader;
import example.THLAPIs.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf07Pagination;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewPaginationSimulation extends Simulation {
    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");
    ScenarioBuilder scn = perf07Pagination(admintoken1, "66252f12e0313ea0b127ef7a");
    {
        setUp(scn
                .injectOpen(rampUsers(10)
                        .during(Duration.ofMinutes(1))))
                .protocols(HttpConfig.baseConfig())
                .assertions(global().successfulRequests().percent().gt(95.0),
                        global().responseTime().percentile3().lt(5000));
    }
}
