package example.THLAPIs;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.status;

/**
 * Reusable THL interview chains derived from the interview performance plan.
 *
 * <p>The class name intentionally keeps the requested "Sceanrios" spelling so
 * existing simulations can import it exactly as requested.</p>
 */
public final class THLinterviewSceanrios {

    private static final AtomicLong UNIQUE_SEQUENCE = new AtomicLong();
    private static final Duration THINK_TIME = Duration.ofSeconds(1);

    private THLinterviewSceanrios() {
    }

    /** Creates unique schedule fields for every virtual-user execution. */
    public static ChainBuilder prepareUniqueScheduleData(
            String hostId,
            String timezoneId,
            String applicantEmailDomain,
            String scheduleType,
            String interviewType) {
        return exec(session -> {
            String suffix = System.currentTimeMillis()
                    + "-" + UNIQUE_SEQUENCE.incrementAndGet()
                    + "-" + UUID.randomUUID().toString().substring(0, 8);
            LocalDate start = LocalDate.now().plusDays(2);
            String domain = applicantEmailDomain == null
                    || applicantEmailDomain.trim().isEmpty()
                    ? "example.test"
                    : applicantEmailDomain.trim();

            return session
                    .set("scheduleUniqueId", suffix)
                    .set("scheduleType", scheduleType)
                    .set("hostId", hostId)
                    .set("applicantEmails",
                            Collections.singletonList("thl-perf+" + suffix + "@" + domain))
                    .set("onlySpecificDay", false)
                    .set("startDay", start.toString())
                    .set("endDay", start.plusDays(1).toString())
                    .set("day", start.toString())
                    .set("fromTime", "09:00")
                    .set("toTime", "12:00")
                    .set("updatedFromTime", "13:00")
                    .set("updatedToTime", "16:00")
                    .set("timezoneId", timezoneId)
                    .set("interviewType", interviewType)
                    .set("location", "THL performance room " + suffix)
                    .set("updatedLocation", "THL updated performance room " + suffix)
                    .set("durationInMinutes", 30)
                    .set("bufferBetweenSlots", 5)
                    .set("maxInterviewsPerDay", 10)
                    .set("slotsCandidateCanSee", 5)
                    .set("cancellationDeadlineInHours", 24)
                    .set("additionalInstructions", "Performance schedule " + suffix)
                    .set("updatedAdditionalInstructions", "Updated performance schedule " + suffix);
        });
    }

    /** Generates message content at execution time to avoid duplicate test data. */
    public static ChainBuilder prepareUniqueInterviewMessage(String contentPrefix) {
        return exec(session -> {
            String prefix = contentPrefix == null || contentPrefix.trim().isEmpty()
                    ? "THL performance message"
                    : contentPrefix.trim();
            String suffix = System.currentTimeMillis()
                    + "-" + UNIQUE_SEQUENCE.incrementAndGet()
                    + "-" + UUID.randomUUID().toString().substring(0, 8);
            return session.set("interviewMessageContent", prefix + " " + suffix);
        });
    }

    /** POST schedule -> list -> PATCH -> list -> DELETE. */
    public static ChainBuilder schedulingLifecycle(
            String token,
            String organizationId,
            String hostId,
            String timezoneId,
            String applicantEmailDomain,
            String scheduleType,
            String interviewType) {
        return prepareUniqueScheduleData(hostId, timezoneId, applicantEmailDomain,
                scheduleType, interviewType)
                .exec(THLInterviewRequests.createInterviewSchedule(token, organizationId)
                        .check(successfulWriteStatus())
                        .check(jsonPath("$.data.id").optional().saveAs("interviewScheduleId"))
                        .check(jsonPath("$.id").optional().saveAs("interviewScheduleId")))
                .exitHereIfFailed()
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.getInterviewSchedules(
                                token, organizationId, scheduleType, interviewType,
                                "#{scheduleUniqueId}", null, 1, 20)
                        .check(readStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.updateInterviewSchedule(
                                token, organizationId, "#{interviewScheduleId}")
                        .check(successfulWriteStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.getInterviewSchedules(
                                token, organizationId, scheduleType, interviewType,
                                "#{scheduleUniqueId}", null, 1, 20)
                        .check(readStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.deleteInterviewSchedule(
                                token, organizationId, "#{interviewScheduleId}")
                        .check(successfulWriteStatus()));
    }

    /** Read-heavy dashboard workload from the linked performance plan. */
    public static ChainBuilder heavyReadFlow(
            String token,
            String organizationId,
            String interviewId,
            String fromDate,
            String toDate) {
        return exec(THLInterviewRequests.getInterviews(
                        token, organizationId, null, null, null,
                        null, fromDate, toDate, 1, 20).check(readStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.getAllInterviews(
                        token, organizationId, fromDate, toDate).check(readStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.getUpcomingInterviews(
                        token, organizationId).check(readStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.getInterviewById(
                        token, organizationId, interviewId).check(readStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.getInterviewReportById(
                        token, organizationId, interviewId).check(readStatus()));
    }

    /** Admin-controlled interview lifecycle; candidate join is intentionally separate. */
    public static ChainBuilder liveInterviewLifecycle(
            String token,
            String organizationId,
            String interviewId) {
        return exec(THLInterviewRequests.getInterviewById(
                        token, organizationId, interviewId).check(readStatus()))
                .exitHereIfFailed()
                .pause(THINK_TIME)
                .exec(prepareUniqueInterviewMessage("THL lifecycle message"))
                .exec(THLInterviewRequests.sendInterviewMessage(
                        token, organizationId, interviewId, "#{interviewMessageContent}")
                        .check(successfulWriteStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.getPaginatedInterviewMessages(
                        token, organizationId, interviewId, 1, 20).check(readStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.pauseInterviewRecording(
                        token, organizationId, interviewId).check(successfulWriteStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.resumeInterviewRecording(
                        token, organizationId, interviewId).check(successfulWriteStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.finishInterview(
                        token, organizationId, interviewId).check(successfulWriteStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.getInterviewById(
                        token, organizationId, interviewId).check(readStatus()))
                .exec(THLInterviewRequests.getInterviewReportById(
                        token, organizationId, interviewId).check(readStatus()));
    }

    /** POST one unique message, then read the requested messages page. */
    public static ChainBuilder interviewMessagingFlow(
            String token,
            String organizationId,
            String interviewId,
            String contentPrefix,
            int page,
            int limit) {
        if (page < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal to 1");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be greater than or equal to 1");
        }

        return prepareUniqueInterviewMessage(contentPrefix)
                .exec(THLInterviewRequests.sendInterviewMessage(
                        token, organizationId, interviewId, "#{interviewMessageContent}")
                        .check(successfulWriteStatus()))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.getPaginatedInterviewMessages(
                        token, organizationId, interviewId, page, limit)
                        .check(readStatus()));
    }

    public static ChainBuilder sendInterviewMessage(
            String token,
            String organizationId,
            String interviewId,
            String contentPrefix) {
        return prepareUniqueInterviewMessage(contentPrefix)
                .exec(THLInterviewRequests.sendInterviewMessage(
                        token, organizationId, interviewId, "#{interviewMessageContent}")
                        .check(successfulWriteStatus()));
    }

    public static ChainBuilder getPaginatedInterviewMessages(
            String token,
            String organizationId,
            String interviewId,
            int page,
            int limit) {
        return exec(THLInterviewRequests.getPaginatedInterviewMessages(
                token, organizationId, interviewId, page, limit).check(readStatus()));
    }

    /** Single actions for concurrency/spike profiles without sharing a mutable chain. */
    /** Candidate-only join chain. Never pass the admin token to this function. */
    public static ChainBuilder candidateJoinInterview(
            String candidateToken,
            String organizationId,
            String interviewId) {
        return exec(THLInterviewRequests.candidateJoinInterview(
                        candidateToken, organizationId, interviewId)
                .check(nonServerErrorStatus()));
    }

    public static ChainBuilder finishInterview(String token, String organizationId, String interviewId) {
        return exec(THLInterviewRequests.finishInterview(token, organizationId, interviewId)
                .check(nonServerErrorStatus()));
    }

    public static ChainBuilder pauseInterview(String token, String organizationId, String interviewId) {
        return exec(THLInterviewRequests.pauseInterviewRecording(token, organizationId, interviewId)
                .check(nonServerErrorStatus()));
    }

    public static ChainBuilder resumeInterview(String token, String organizationId, String interviewId) {
        return exec(THLInterviewRequests.resumeInterviewRecording(token, organizationId, interviewId)
                .check(nonServerErrorStatus()));
    }

    public static ChainBuilder cancelInterview(String token, String organizationId, String interviewId) {
        return exec(THLInterviewRequests.cancelInterview(token, organizationId, interviewId)
                .check(nonServerErrorStatus()));
    }

    public static ChainBuilder updateInterview(
            String token,
            String organizationId,
            String interviewId,
            String hostId,
            String aiAgentId) {
        return exec(session -> session
                    .set("updatedHostId", hostId)
                    .set("updatedAiAgentId", aiAgentId))
                .exec(THLInterviewRequests.updateInterview(token, organizationId, interviewId)
                        .check(successfulWriteStatus()));
    }

    public static ChainBuilder kickUser(String token, String organizationId, String interviewId) {
        return exec(THLInterviewRequests.kickUserFromInterview(token, organizationId, interviewId)
                .check(nonServerErrorStatus()));
    }

    public static ChainBuilder paginatedInterviews(
            String token,
            String organizationId,
            int page,
            int limit) {
        return exec(THLInterviewRequests.getInterviews(
                token, organizationId, null, null, null,
                null, null, null, page, limit).check(readStatus()));
    }

    /** PERF-04: submit the same per-user unique schedule twice, then clean it up. */
    public static ChainBuilder duplicateScheduleSubmission(
            String token,
            String organizationId,
            String hostId,
            String timezoneId,
            String applicantEmailDomain,
            String scheduleType,
            String interviewType) {
        return prepareUniqueScheduleData(hostId, timezoneId, applicantEmailDomain,
                scheduleType, interviewType)
                .exec(THLInterviewRequests.createInterviewSchedule(token, organizationId)
                        .check(successfulWriteStatus())
                        .check(jsonPath("$.data.id").optional().saveAs("interviewScheduleId"))
                        .check(jsonPath("$.id").optional().saveAs("interviewScheduleId")))
                .exitHereIfFailed()
                .exec(THLInterviewRequests.createInterviewSchedule(token, organizationId)
                        .check(nonServerErrorStatus()))
                .doIf(session -> session.contains("interviewScheduleId")).then(
                        exec(THLInterviewRequests.deleteInterviewSchedule(
                                token, organizationId, "#{interviewScheduleId}")
                                .check(nonServerErrorStatus()))
                );
    }

    /** PERF-07 page-depth and page-size matrix. */
    public static ChainBuilder paginationMatrix(String token, String organizationId) {
        return exec(paginatedInterviews(token, organizationId, 1, 10))
                .exec(paginatedInterviews(token, organizationId, 1, 50))
                .exec(paginatedInterviews(token, organizationId, 1, 100))
                .exec(paginatedInterviews(token, organizationId, 100, 100))
                .exec(paginatedInterviews(token, organizationId, 1000, 100));
    }

    public static ScenarioBuilder schedulingScenario(
            String token,
            String organizationId,
            String hostId,
            String timezoneId,
            String applicantEmailDomain,
            String scheduleType,
            String interviewType) {
        return scenario("THL - Scheduling lifecycle")
                .exec(schedulingLifecycle(token, organizationId, hostId, timezoneId,
                        applicantEmailDomain, scheduleType, interviewType));
    }

    public static ScenarioBuilder liveInterviewScenario(
            String token,
            String organizationId,
            String interviewId) {
        return scenario("THL - Live interview lifecycle")
                .exec(liveInterviewLifecycle(token, organizationId, interviewId));
    }

    public static ScenarioBuilder heavyReadScenario(
            String token,
            String organizationId,
            String interviewId,
            String fromDate,
            String toDate) {
        return scenario("THL - Interview dashboard reads")
                .exec(heavyReadFlow(token, organizationId, interviewId, fromDate, toDate));
    }

    public static ScenarioBuilder perf01Baseline(
            String token, String organizationId, String interviewId,
            String fromDate, String toDate) {
        return scenario("PERF-01 - THL Interview baseline")
                .exec(heavyReadFlow(token, organizationId, interviewId, fromDate, toDate));
    }

    public static ScenarioBuilder perf02NormalLoad(
            String token, String organizationId, String interviewId,
            String fromDate, String toDate) {
        return scenario("PERF-02 - THL Interview normal load")
                .exec(heavyReadFlow(token, organizationId, interviewId, fromDate, toDate))
                .exec(interviewMessagingFlow(
                        token, organizationId, interviewId, "THL normal-load message", 1, 20));
    }

    public static ScenarioBuilder perf03ConcurrentInterviewOperations(
            String token, String organizationId, String interviewId) {
        return scenario("PERF-03 - THL concurrent interview operations")
                .exec(sendInterviewMessage(
                        token, organizationId, interviewId, "THL concurrent message"))
                .exec(pauseInterview(token, organizationId, interviewId))
                .exec(resumeInterview(token, organizationId, interviewId))
                .exec(finishInterview(token, organizationId, interviewId));
    }

    /** Dedicated candidate-authenticated join scenario. */
    public static ScenarioBuilder candidateJoinScenario(
            String candidateToken,
            String organizationId,
            String interviewId) {
        return scenario("THL - Candidate joins interview")
                .exec(candidateJoinInterview(candidateToken, organizationId, interviewId));
    }

    public static ScenarioBuilder perf04SchedulingConcurrency(
            String token, String organizationId, String hostId, String timezoneId,
            String applicantEmailDomain, String scheduleType, String interviewType) {
        return scenario("PERF-04 - THL scheduling concurrency")
                .exec(duplicateScheduleSubmission(token, organizationId, hostId, timezoneId,
                        applicantEmailDomain, scheduleType, interviewType));
    }

    public static ScenarioBuilder perf05InterviewLifecycle(
            String token, String organizationId, String interviewId) {
        return scenario("PERF-05 - THL interview lifecycle")
                .exec(liveInterviewLifecycle(token, organizationId, interviewId));
    }

    public static ScenarioBuilder perf06ListingVolume(
            String token, String organizationId, String interviewId,
            String fromDate, String toDate) {
        return scenario("PERF-06 - THL listing and volume")
                .repeat(5).on(heavyReadFlow(
                        token, organizationId, interviewId, fromDate, toDate));
    }

    public static ScenarioBuilder perf07Pagination(String token, String organizationId) {
        return scenario("PERF-07 - THL pagination")
                .exec(paginationMatrix(token, organizationId));
    }

    public static ScenarioBuilder perf08Spike(
            String token, String organizationId, String interviewId,
            String fromDate, String toDate) {
        return scenario("PERF-08 - THL interview spike")
                .exec(THLInterviewRequests.getInterviewById(
                        token, organizationId, interviewId).check(readStatus()))
                .exec(interviewMessagingFlow(
                        token, organizationId, interviewId, "THL spike message", 1, 20))
                .exec(THLInterviewRequests.getAllInterviews(
                        token, organizationId, fromDate, toDate).check(readStatus()));
    }

    public static ScenarioBuilder perf09Stress(
            String token, String organizationId, String interviewId) {
        return scenario("PERF-09 - THL interview stress")
                .exec(THLInterviewRequests.getInterviews(
                        token, organizationId, null, null, null,
                        null, null, null, 1, 20).check(readStatus()))
                .exec(THLInterviewRequests.getInterviewById(
                        token, organizationId, interviewId).check(readStatus()))
                .exec(interviewMessagingFlow(
                        token, organizationId, interviewId, "THL stress message", 1, 20));
    }

    public static ScenarioBuilder perf10Soak(
            String token, String organizationId, String interviewId) {
        return scenario("PERF-10 - THL interview soak")
                .during(Duration.ofMinutes(10)).on(
                        interviewMessagingFlow(token, organizationId, interviewId,
                                "THL soak message", 1, 50).pause(THINK_TIME));
    }

    public static ScenarioBuilder perf11RateLimit(
            String token, String organizationId, String interviewId) {
        return scenario("PERF-11 - THL rate limiting")
                .exec(sendInterviewMessage(
                        token, organizationId, interviewId, "THL rate-limit message"));
    }

    public static ScenarioBuilder perf12RecoveryAndDataIntegrity(
            String token, String organizationId, String interviewId) {
        return scenario("PERF-12 - THL recovery and data integrity")
                .exec(cancelInterview(token, organizationId, interviewId))
                .pause(THINK_TIME)
                .exec(THLInterviewRequests.getInterviewById(
                        token, organizationId, interviewId).check(readStatus()));
    }

    private static io.gatling.javaapi.core.CheckBuilder.Final readStatus() {
        return status().is(200);
    }

    private static io.gatling.javaapi.core.CheckBuilder.Final successfulWriteStatus() {
        return status().in(200, 201, 202, 204);
    }

    private static io.gatling.javaapi.core.CheckBuilder.Final nonServerErrorStatus() {
        return status().lt(500);
    }
}
