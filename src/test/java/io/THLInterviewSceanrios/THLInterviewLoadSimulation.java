package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf02NormalLoad;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewLoadSimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf02NormalLoad(admintoken1, organizationId(), interviewId(), fromDate(), toDate())
                .injectOpen(
                        rampUsers(50).during(Duration.ofSeconds(30)),
                        rampUsers(100).during(Duration.ofMinutes(1)),
                        rampUsers(200).during(Duration.ofMinutes(2))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(95.0, 5000));
    }
}
