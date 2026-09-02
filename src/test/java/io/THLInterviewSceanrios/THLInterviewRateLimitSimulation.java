package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf11RateLimit;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewRateLimitSimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf11RateLimit(admintoken1, organizationId(), interviewId())
                .injectOpen(constantUsersPerSec(integer("thl.rateLimitRps", 100))
                        .during(Duration.ofMinutes(1))))
                .protocols(httpProtocol)
                .assertions(global().responseTime().percentile3().lt(10000));
    }
}
