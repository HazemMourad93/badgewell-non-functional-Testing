package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf05InterviewLifecycle;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewLifecycleSimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf05InterviewLifecycle(admintoken1, organizationId(), interviewId())
                .injectOpen(constantUsersPerSec(integer("thl.lifecycleUsersPerSecond", 5))
                        .during(Duration.ofMinutes(5))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(95.0, 5000));
    }
}
