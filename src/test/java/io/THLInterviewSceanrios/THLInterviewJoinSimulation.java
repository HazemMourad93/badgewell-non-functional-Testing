package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.candidateJoinScenario;
import static io.gatling.javaapi.core.CoreDsl.*;

/** Candidate-authenticated join traffic, isolated from all admin scenarios. */
public class THLInterviewJoinSimulation extends THLInterviewSimulationSupport {
    {
        setUp(candidateJoinScenario(candtoken, organizationId(), interviewId())
                .injectOpen(
                        rampUsers(integer("thl.joinWarmupUsers", 20))
                                .during(Duration.ofSeconds(20)),
                        stressPeakUsers(integer("thl.joinPeakUsers", 500))
                                .during(Duration.ofSeconds(30))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(90.0, 10000));
    }
}
