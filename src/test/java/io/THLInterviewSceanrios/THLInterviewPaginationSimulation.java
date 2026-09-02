package io.THLInterviewSceanrios;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf07Pagination;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewPaginationSimulation extends THLInterviewSimulationSupport {
    {
        setUp(perf07Pagination(admintoken1, organizationId())
                .injectOpen(rampUsers(integer("thl.paginationUsers", 10))
                        .during(Duration.ofMinutes(1))))
                .protocols(httpProtocol)
                .assertions(standardAssertions(95.0, 5000));
    }
}
