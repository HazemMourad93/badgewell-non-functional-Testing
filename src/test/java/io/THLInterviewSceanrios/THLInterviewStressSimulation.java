package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf09Stress;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewStressSimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf09Stress(admintoken1, organizationId(), interviewId())
                .injectOpen(stressPeakUsers(integer("thl.stressUsers", 1500))
                        .during(Duration.ofMinutes(7))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(85.0, 15000));
    }
}
