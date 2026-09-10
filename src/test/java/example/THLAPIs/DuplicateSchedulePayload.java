package example.THLAPIs;

import org.json.simple.JSONObject;

import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/** Payload generation exclusively for duplicate schedule submissions. */
final class DuplicateSchedulePayload {
    private DuplicateSchedulePayload() { }

    static String create(String hostId, String timezoneId, String applicantEmail,
                         String scheduleType, String interviewType, Clock clock) {
        ZoneId zone = ZoneId.of(timezoneId);
        ZonedDateTime now = ZonedDateTime.now(clock.withZone(zone));
        // Leave the cancellation lead time plus a minute before the earliest slot.
        ZonedDateTime earliest = now.plusHours(2).plusMinutes(1).withSecond(0).withNano(0);
        LocalDate lastDay = YearMonth.from(now).atEndOfMonth();
        ZonedDateTime latest = lastDay.atTime(21, 59).atZone(zone);
        if (earliest.isAfter(latest)) {
            throw new IllegalStateException("No future two-hour schedule fits in the current month with two-hour notice");
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        LocalDate day = LocalDate.ofEpochDay(random.nextLong(
                earliest.toLocalDate().toEpochDay(), lastDay.toEpochDay() + 1));
        int firstMinute = day.equals(earliest.toLocalDate())
                ? earliest.getHour() * 60 + earliest.getMinute() : 0;
        // If today has no room, choose a remaining day instead.
        if (firstMinute > 21 * 60 + 59) {
            day = LocalDate.ofEpochDay(random.nextLong(day.plusDays(1).toEpochDay(),
                    lastDay.toEpochDay() + 1));
            firstMinute = 0;
        }
        int minute = random.nextInt(firstMinute, 22 * 60);
        ZonedDateTime from = day.atStartOfDay(zone).withHour(minute / 60).withMinute(minute % 60);
        ZonedDateTime to = from.plusMinutes(120);
        String unique = UUID.randomUUID().toString().replace("-", "");
        String email = uniqueEmail(applicantEmail, unique);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("scheduleType", scheduleType);
        body.put("hostId", hostId);
        body.put("applicantEmails", Collections.singletonList(email));
        body.put("onlySpecificDay", true);
        body.put("day", day.atStartOfDay(zone).toInstant().toString());
        body.put("fromTime", from.toInstant().toString());
        body.put("toTime", to.toInstant().toString());
        body.put("timezoneId", timezoneId);
        body.put("interviewType", interviewType);
        if ("IN_PERSON".equals(interviewType)) {
            body.put("location", "THL performance room " + unique);
        }
        body.put("durationInMinutes", 120);
        body.put("bufferBetweenSlots", 0);
        body.put("maxInterviewsPerDay", 1);
        body.put("numberOfCandidates", 1);
        body.put("cancellationDeadlineInHours", 2);
        return JSONObject.toJSONString(body);
    }

    private static String uniqueEmail(String input, String unique) {
        String value = input == null || input.trim().isEmpty() ? "example.test" : input.trim();
        int at = value.indexOf('@');
        String local = at < 0 ? "thl-perf" : value.substring(0, at).split("\\+", 2)[0];
        String domain = at < 0 ? value : value.substring(at + 1);
        if (local.isEmpty() || local.length() > 26 || domain.isEmpty()
                || domain.contains("@") || value.matches(".*\\s.*")) {
            throw new IllegalArgumentException("Provide a valid applicant email or domain");
        }
        return local + "+perf-" + unique + "@" + domain;
    }
}
