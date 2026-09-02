package example.THLAPIs;

import io.gatling.javaapi.http.HttpRequestActionBuilder;
import org.json.simple.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.http.HttpDsl.http;

/**
 * Pure Gatling HTTP request builders for the THL interview module.
 *
 * <p>Arguments may be literal values or Gatling EL expressions such as
 * {@code #{token}}, {@code #{organizationId}} and {@code #{interviewId}}.</p>
 */
public final class THLInterviewRequests {

    private static final String ORGANIZATIONS = "/api/v1/organizations/";

    private THLInterviewRequests() {
    }

    public static HttpRequestActionBuilder createInterviewSchedule(
            String token,
            String organizationId) {
        return http("THL - Create interview schedule")
                .post(ORGANIZATIONS + organizationId + "/interview-schedules")
                .header("Authorization", bearer(token))
                .body(StringBody(session -> scheduleBody(session, false)))
                .asJson();
    }

    public static HttpRequestActionBuilder updateInterviewSchedule(
            String token,
            String organizationId,
            String interviewScheduleId) {
        return http("THL - Update interview schedule")
                .patch(ORGANIZATIONS + organizationId
                        + "/interview-schedules/" + interviewScheduleId)
                .header("Authorization", bearer(token))
                .body(StringBody(session -> scheduleBody(session, true)))
                .asJson();
    }

    public static HttpRequestActionBuilder deleteInterviewSchedule(
            String token,
            String organizationId,
            String interviewScheduleId) {
        return http("THL - Delete interview schedule")
                .delete(ORGANIZATIONS + organizationId
                        + "/interview-schedules/" + interviewScheduleId)
                .header("Authorization", bearer(token))
                .header("Accept", "application/json");
    }

    public static HttpRequestActionBuilder getInterviewSchedules(
            String token,
            String organizationId,
            String scheduleType,
            String interviewType,
            String search,
            String jobAdId,
            Object page,
            Object limit) {
        HttpRequestActionBuilder request = http("THL - Get interview schedules")
                .get(ORGANIZATIONS + organizationId + "/interview-schedules")
                .header("Authorization", bearer(token))
                .header("Accept", "application/json");

        request = addQueryParam(request, "scheduleType", scheduleType);
        request = addQueryParam(request, "interviewType", interviewType);
        request = addQueryParam(request, "search", search);
        request = addQueryParam(request, "jobAdId", jobAdId);
        request = addQueryParam(request, "page", page);
        return addQueryParam(request, "limit", limit);
    }

    public static HttpRequestActionBuilder getInterviews(
            String token,
            String organizationId,
            String scheduleId,
            String scheduleType,
            String interviewType,
            String status,
            String fromDate,
            String toDate,
            Object page,
            Object limit) {
        HttpRequestActionBuilder request = http("THL - Get interviews")
                .get(ORGANIZATIONS + organizationId + "/interviews")
                .header("Authorization", bearer(token))
                .header("Accept", "application/json");

        request = addQueryParam(request, "scheduleId", scheduleId);
        request = addQueryParam(request, "scheduleType", scheduleType);
        request = addQueryParam(request, "interviewType", interviewType);
        request = addQueryParam(request, "status", status);
        request = addQueryParam(request, "fromDate", fromDate);
        request = addQueryParam(request, "toDate", toDate);
        request = addQueryParam(request, "page", page);
        return addQueryParam(request, "limit", limit);
    }

    public static HttpRequestActionBuilder getAllInterviews(
            String token,
            String organizationId,
            String fromDate,
            String toDate) {
        HttpRequestActionBuilder request = http("THL - Get all interviews")
                .get(ORGANIZATIONS + organizationId + "/interviews/all")
                .header("Authorization", bearer(token))
                .header("Accept", "application/json");
        request = addQueryParam(request, "fromDate", fromDate);
        return addQueryParam(request, "toDate", toDate);
    }

    public static HttpRequestActionBuilder getUpcomingInterviews(
            String token,
            String organizationId) {
        return http("THL - Get upcoming interviews")
                .get(ORGANIZATIONS + organizationId + "/interviews/upcoming")
                .header("Authorization", bearer(token))
                .header("Accept", "application/json");
    }

    public static HttpRequestActionBuilder getInterviewById(
            String token,
            String organizationId,
            String interviewId) {
        return http("THL - Get interview by ID")
                .get(ORGANIZATIONS + organizationId + "/interviews/" + interviewId)
                .header("Authorization", bearer(token))
                .header("Accept", "application/json");
    }

    public static HttpRequestActionBuilder getInterviewReportById(
            String token,
            String organizationId,
            String interviewId) {
        return http("THL - Get interview report")
                .get(ORGANIZATIONS + organizationId
                        + "/interviews/" + interviewId + "/report")
                .header("Authorization", bearer(token))
                .header("Accept", "application/json");
    }

    public static HttpRequestActionBuilder getPaginatedInterviewMessages(
            String token,
            String organizationId,
            String interviewId,
            Object page,
            Object limit) {
        if (page instanceof Number && ((Number) page).intValue() < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal to 1");
        }
        if (limit instanceof Number && ((Number) limit).intValue() < 1) {
            throw new IllegalArgumentException("Limit must be greater than or equal to 1");
        }

        return http("THL - Get paginated interview messages")
                .get(ORGANIZATIONS + organizationId
                        + "/interviews/" + interviewId + "/interview-messages")
                .queryParam("page", page)
                .queryParam("limit", limit)
                .header("Authorization", bearer(token))
                .header("Accept", "application/json");
    }

    public static HttpRequestActionBuilder sendInterviewMessage(
            String token,
            String organizationId,
            String interviewId,
            String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content must not be null or empty");
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("content", content);

        return http("THL - Send interview message")
                .post(ORGANIZATIONS + organizationId
                        + "/interviews/" + interviewId + "/interview-messages")
                .header("Authorization", bearer(token))
                .body(StringBody(JSONObject.toJSONString(body)))
                .asJson();
    }

    public static HttpRequestActionBuilder candidateJoinInterview(
            String candidateToken,
            String organizationId,
            String interviewId) {
        return interviewCommand("THL - Candidate joins interview", candidateToken,
                organizationId, interviewId, "join");
    }

    public static HttpRequestActionBuilder pauseInterviewRecording(
            String token,
            String organizationId,
            String interviewId) {
        return interviewCommand("THL - Pause interview recording", token,
                organizationId, interviewId, "pause");
    }

    public static HttpRequestActionBuilder resumeInterviewRecording(
            String token,
            String organizationId,
            String interviewId) {
        return interviewCommand("THL - Resume interview recording", token,
                organizationId, interviewId, "resume");
    }

    public static HttpRequestActionBuilder finishInterview(
            String token,
            String organizationId,
            String interviewId) {
        return interviewCommand("THL - Finish interview", token,
                organizationId, interviewId, "finish");
    }

    public static HttpRequestActionBuilder kickUserFromInterview(
            String token,
            String organizationId,
            String interviewId) {
        return interviewCommand("THL - Kick user from interview", token,
                organizationId, interviewId, "kick-user");
    }

    public static HttpRequestActionBuilder cancelInterview(
            String token,
            String organizationId,
            String interviewId) {
        return http("THL - Cancel interview")
                .patch(ORGANIZATIONS + organizationId
                        + "/interviews/" + interviewId + "/cancel")
                .header("Authorization", bearer(token))
                .header("Accept", "application/json");
    }

    public static HttpRequestActionBuilder updateInterview(
            String token,
            String organizationId,
            String interviewId) {
        return http("THL - Update interview")
                .patch(ORGANIZATIONS + organizationId + "/interviews/" + interviewId)
                .header("Authorization", bearer(token))
                .body(StringBody(session -> {
                    Map<String, Object> body = new LinkedHashMap<>();
                    putIfPresent(body, "hostId", session.get("updatedHostId"));
                    putIfPresent(body, "aiAgentId", session.get("updatedAiAgentId"));
                    return JSONObject.toJSONString(body);
                }))
                .asJson();
    }

    private static HttpRequestActionBuilder interviewCommand(
            String requestName,
            String token,
            String organizationId,
            String interviewId,
            String command) {
        return http(requestName)
                .post(ORGANIZATIONS + organizationId
                        + "/interviews/" + interviewId + "/" + command)
                .header("Authorization", bearer(token))
                .header("Accept", "application/json");
    }

    private static HttpRequestActionBuilder addQueryParam(
            HttpRequestActionBuilder request,
            String name,
            Object value) {
        if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
            return request;
        }
        return request.queryParam(name, value);
    }

    private static String scheduleBody(io.gatling.javaapi.core.Session session, boolean update) {
        Map<String, Object> body = new LinkedHashMap<>();
        putIfPresent(body, "scheduleType", session.get("scheduleType"));
        putIfPresent(body, "hostId", session.get("hostId"));
        putIfPresent(body, "applicantEmails", session.get("applicantEmails"));
        putIfPresent(body, "onlySpecificDay", session.get("onlySpecificDay"));
        putIfPresent(body, "startDay", session.get("startDay"));
        putIfPresent(body, "endDay", session.get("endDay"));
        putIfPresent(body, "day", session.get("day"));
        putIfPresent(body, "fromTime", session.get(update ? "updatedFromTime" : "fromTime"));
        putIfPresent(body, "toTime", session.get(update ? "updatedToTime" : "toTime"));
        putIfPresent(body, "timezoneId", session.get("timezoneId"));
        putIfPresent(body, "blockStartDate", session.get("blockStartDate"));
        putIfPresent(body, "blockEndDate", session.get("blockEndDate"));
        putIfPresent(body, "blockReason", session.get("blockReason"));
        putIfPresent(body, "interviewType", session.get("interviewType"));
        putIfPresent(body, "location", session.get(update ? "updatedLocation" : "location"));
        putIfPresent(body, "durationInMinutes", session.get("durationInMinutes"));
        putIfPresent(body, "bufferBetweenSlots", session.get("bufferBetweenSlots"));
        putIfPresent(body, "maxInterviewsPerDay", session.get("maxInterviewsPerDay"));
        putIfPresent(body, "slotsCandidateCanSee", session.get("slotsCandidateCanSee"));
        putIfPresent(body, "cancellationDeadlineInHours",
                session.get("cancellationDeadlineInHours"));
        putIfPresent(body, "additionalInstructions",
                session.get(update ? "updatedAdditionalInstructions" : "additionalInstructions"));
        return JSONObject.toJSONString(body);
    }

    private static void putIfPresent(Map<String, Object> body, String name, Object value) {
        if (value != null && (!(value instanceof String) || !((String) value).trim().isEmpty())) {
            body.put(name, value);
        }
    }

    private static String bearer(String token) {
        return "Bearer " + token;
    }
}
