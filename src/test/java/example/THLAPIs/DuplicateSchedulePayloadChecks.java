package example.THLAPIs;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.time.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Offline checks: run this main after test-compile; no HTTP requests are made. */
public final class DuplicateSchedulePayloadChecks {
    public static void main(String[] args) {
        ZoneId zone = ZoneId.of("Africa/Cairo");
        Set<String> emails = new HashSet<>();
        Set<LocalDate> days = new HashSet<>();
        Set<Integer> hours = new HashSet<>();
        for (String instant : new String[]{"2026-09-10T11:00:00Z", "2026-09-29T20:00:00Z",
                "2026-09-30T10:00:00Z", "2028-02-28T10:00:00Z", "2026-12-31T10:00:00Z"}) {
            Clock clock = Clock.fixed(Instant.parse(instant), zone);
            for (int i = 0; i < 500; i++) {
                JSONObject body = (JSONObject) JSONValue.parse(DuplicateSchedulePayload.create(
                        "6a82fa1e2d30faf0f9aa7572", zone.getId(), "hazem.farag+77@badgewell.com",
                        "HOST", "ONLINE", clock));
                ZonedDateTime from = Instant.parse((String) body.get("fromTime")).atZone(zone);
                ZonedDateTime to = Instant.parse((String) body.get("toTime")).atZone(zone);
                LocalDate day = Instant.parse((String) body.get("day")).atZone(zone).toLocalDate();
                require(YearMonth.from(from).equals(YearMonth.now(clock)), "Wrong month");
                require(from.toLocalDate().equals(day) && to.toLocalDate().equals(day), "Cross-day window");
                require(from.toInstant().isAfter(clock.instant().plusSeconds(7200)), "Insufficient notice");
                require(Duration.between(from, to).toMinutes() == 120, "Wrong duration");
                String email = (String) ((List<?>) body.get("applicantEmails")).get(0);
                require(email.matches("hazem\\.farag\\+perf-[a-f0-9]{32}@badgewell\\.com"), "Invalid email");
                require(emails.add(email), "Duplicate applicant");
                require(Boolean.TRUE.equals(body.get("onlySpecificDay")), "Not a specific day");
                require(!body.containsKey("id") && !body.containsKey("createdAt")
                        && !body.containsKey("startDay") && !body.containsKey("location"), "Unexpected fields");
                require(Long.valueOf(1).equals(body.get("numberOfCandidates")), "Wrong candidate count");
                days.add(day);
                hours.add(from.getHour());
            }
        }
        require(days.size() > 10 && hours.size() > 10, "Dates/times are not varied");
        try {
            DuplicateSchedulePayload.create("host", zone.getId(), "example.test", "HOST", "ONLINE",
                    Clock.fixed(Instant.parse("2026-09-30T20:00:00Z"), zone));
            throw new AssertionError("Expected current-month exhaustion to fail");
        } catch (IllegalStateException expected) { }
        System.out.println("PASS: 2,500 unique payloads; future current-month dates, month-end/leap-year boundaries, durations and emails");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
