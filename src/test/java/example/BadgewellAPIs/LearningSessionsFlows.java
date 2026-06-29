package example.BadgewellAPIs;



import io.gatling.javaapi.core.ChainBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class LearningSessionsFlows {


    public static ChainBuilder getLearningSessionsBasicFlow(
            int organizationId,
            int page,
            int limit,
            String sortType,
            String search,
            String token
    ) {
        return exec(
                LearningSessionsRequests.getLearningSessions(
                        organizationId, page, limit, sortType, search, token
                ).check(
                        status().is(200),
                        status().not(500),
                        status().not(503),
                        status().not(504)
                )
        ).pause(Duration.ofMillis(4000));
    }
}