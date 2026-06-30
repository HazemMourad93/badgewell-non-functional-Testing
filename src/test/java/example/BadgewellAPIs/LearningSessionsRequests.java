package example.BadgewellAPIs;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static io.gatling.javaapi.http.HttpDsl.*;

public class LearningSessionsRequests {

    public static HttpRequestActionBuilder getLearningSessions(
            int organizationId,
            Object page,
            int limit,
            String sortType,
            String search,
            String token
    ) {
        return http("Get learning sessions")
                .get("/api/organizations/" + organizationId + "/learning/sessions")
                .queryParam("page", page)
                .queryParam("limit", limit)
                .queryParam("sortType", sortType)
                .queryParam("search", search)
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json");
    }




}