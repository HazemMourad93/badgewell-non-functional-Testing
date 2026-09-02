package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf06ListingVolume;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewVolumeSimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf06ListingVolume(admintoken1, organizationId(), interviewId(), fromDate(), toDate())
                .injectOpen(rampUsers(integer("thl.volumeUsers", 20))
                        .during(Duration.ofMinutes(2))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(95.0, 7500));
    }
}
