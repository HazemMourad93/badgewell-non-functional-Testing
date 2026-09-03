package io.THLInterviewSceanrios;

import example.HelperClassTUT.ConfigReader;
import example.THLAPIs.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf01Baseline;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewBaselineSimulation extends Simulation {
    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");
    String organizationId = "66252f12e0313ea0b127ef7a";
    String interviewId = "6a9921161bb0bd7339a4c268";

    ScenarioBuilder scn = perf01Baseline(admintoken1, organizationId, interviewId,
            "2026-09-01", "2026-09-30");

    {
        setUp(scn
                .injectOpen(
                        atOnceUsers(1),
                        nothingFor(Duration.ofSeconds(5)),
                        atOnceUsers(5),
                        nothingFor(Duration.ofSeconds(5)),
                        atOnceUsers(10)))
                .protocols(HttpConfig.baseConfig())
                .assertions(
                        global().successfulRequests().percent().gte(85.0),
                        global().failedRequests().percent().lt(10.0),
                        global().responseTime().percentile3().lt(4000),
                        global().responseTime().max().lt(90000));
    }
}
