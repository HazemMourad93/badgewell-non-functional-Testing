package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf08Spike;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewSpikeSimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf08Spike(admintoken1, organizationId(), interviewId(), fromDate(), toDate())
                .injectOpen(
                        rampUsers(20).during(Duration.ofSeconds(20)),
                        stressPeakUsers(integer("thl.spikeUsers", 500)).during(Duration.ofSeconds(30)),
                        constantUsersPerSec(5).during(Duration.ofMinutes(1))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(90.0, 10000));
    }
}
