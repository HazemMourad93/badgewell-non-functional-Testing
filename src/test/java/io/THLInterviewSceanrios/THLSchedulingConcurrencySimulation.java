package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf04SchedulingConcurrency;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLSchedulingConcurrencySimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf04SchedulingConcurrency(admintoken1, organizationId(), hostId(), timezoneId(),
                        applicantEmailDomain(), scheduleType(), interviewType())
                .injectOpen(rampUsers(integer("thl.schedulingUsers", 50))
                        .during(Duration.ofSeconds(30))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(95.0, 5000));
    }
}
