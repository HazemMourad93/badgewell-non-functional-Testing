package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf03ConcurrentInterviewOperations;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewConcurrencySimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf03ConcurrentInterviewOperations(admintoken1, organizationId(), interviewId())
                .injectOpen(rampUsers(integer("thl.concurrentUsers", 50))
                        .during(Duration.ofSeconds(30))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(95.0, 5000));
    }
}
