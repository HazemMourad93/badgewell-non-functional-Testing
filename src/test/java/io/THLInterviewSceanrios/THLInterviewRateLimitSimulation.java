package io.THLInterviewSceanrios;

import example.HelperClassTUT.ConfigReader;
import example.THLAPIs.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf11RateLimit;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewRateLimitSimulation extends Simulation {
    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");
    ScenarioBuilder scn = perf11RateLimit(admintoken1, "66252f12e0313ea0b127ef7a",
            "6a89d0315d19d0f6c3182562");
    {
        setUp(scn
                .injectOpen(constantUsersPerSec(100)
                        .during(Duration.ofMinutes(1))))
                .protocols(HttpConfig.baseConfig())
                .assertions(global().responseTime().percentile3().lt(10000));
    }
}
