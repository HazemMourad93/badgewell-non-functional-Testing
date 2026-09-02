package io.THLInterviewSceanrios;

import example.HelperClassTUT.ConfigReader;
import example.THLAPIs.HttpConfig;
import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.LocalDate;

import static io.gatling.javaapi.core.CoreDsl.global;

abstract class THLInterviewSimulationSupport extends Simulation {

    protected final HttpProtocolBuilder httpProtocol = HttpConfig.baseConfig();
    protected final String admintoken1 =
            ConfigReader.properties.getProperty("admintoken1");
    protected final String candtoken =
            ConfigReader.properties.getProperty("candtoken");

    protected String organizationId() {
        return required("thl.organizationId", "THL_ORGANIZATION_ID");
    }

    protected String interviewId() {
        return required("thl.interviewId", "THL_INTERVIEW_ID");
    }

    protected String hostId() {
        return required("thl.hostId", "THL_HOST_ID");
    }

    protected String timezoneId() {
        return required("thl.timezoneId", "THL_TIMEZONE_ID");
    }

    protected String scheduleType() {
        return required("thl.scheduleType", "THL_SCHEDULE_TYPE");
    }

    protected String interviewType() {
        return required("thl.interviewType", "THL_INTERVIEW_TYPE");
    }

    protected String applicantEmailDomain() {
        return value("thl.applicantEmailDomain", "THL_APPLICANT_EMAIL_DOMAIN", "example.test");
    }

    protected String fromDate() {
        return value("thl.fromDate", "THL_FROM_DATE", LocalDate.now().minusDays(30).toString());
    }

    protected String toDate() {
        return value("thl.toDate", "THL_TO_DATE", LocalDate.now().plusDays(30).toString());
    }

    protected int integer(String property, int defaultValue) {
        String configured = value(property, property.toUpperCase().replace('.', '_'), null);
        return configured == null ? defaultValue : Integer.parseInt(configured);
    }

    protected Assertion[] standardAssertions(double successPercent, int p95Millis) {
        return new Assertion[]{
                global().successfulRequests().percent().gte(successPercent),
                global().responseTime().percentile3().lt(p95Millis),
                global().responseTime().max().lt(p95Millis * 3)
        };
    }

    private String required(String property, String environment) {
        String result = value(property, environment, null);
        if (result == null || result.trim().isEmpty()) {
            throw new IllegalStateException("Missing THL setting. Supply -D" + property
                    + "=<value> or environment variable " + environment);
        }
        return result;
    }

    private String value(String property, String environment, String defaultValue) {
        String result = System.getProperty(property);
        if (result == null || result.trim().isEmpty()) {
            result = System.getenv(environment);
        }
        if (result == null || result.trim().isEmpty()) {
            result = ConfigReader.properties.getProperty(property);
        }
        return result == null || result.trim().isEmpty() ? defaultValue : result.trim();
    }
}
