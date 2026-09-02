package io.THLInterviewSceanrios;

import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf01Baseline;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewBaselineSimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf01Baseline(admintoken1, organizationId(), interviewId(), fromDate(), toDate())
                .injectOpen(
                        atOnceUsers(1),
                        nothingFor(Duration.ofSeconds(5)),
                        atOnceUsers(5),
                        nothingFor(Duration.ofSeconds(5)),
                        atOnceUsers(10)))
                .protocols(httpProtocol)
                .assertions(standardAssertions(99.0, 2500));
    }
}
