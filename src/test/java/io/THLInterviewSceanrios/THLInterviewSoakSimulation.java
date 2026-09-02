package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf10Soak;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewSoakSimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf10Soak(admintoken1, organizationId(), interviewId())
                .injectClosed(constantConcurrentUsers(integer("thl.soakConcurrentUsers", 10))
                        .during(Duration.ofHours(integer("thl.soakHours", 1)))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(95.0, 7500));
    }
}
