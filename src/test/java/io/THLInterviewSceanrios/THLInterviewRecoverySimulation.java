package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf12RecoveryAndDataIntegrity;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewRecoverySimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf12RecoveryAndDataIntegrity(admintoken1, organizationId(), interviewId())
                .injectOpen(rampUsers(integer("thl.recoveryUsers", 50))
                        .during(Duration.ofMinutes(1))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(95.0, 7500));
    }
}
