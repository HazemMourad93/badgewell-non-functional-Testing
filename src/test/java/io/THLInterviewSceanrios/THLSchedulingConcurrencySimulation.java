package io.THLInterviewSceanrios;

import example.HelperClassTUT.ConfigReader;
import example.THLAPIs.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf04SchedulingConcurrency;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLSchedulingConcurrencySimulation extends Simulation {
    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");
    ScenarioBuilder scn = perf04SchedulingConcurrency(
            admintoken1,
            "66252f12e0313ea0b127ef7a",
            "678e1da3a37a4d664121a1e7",
            "Africa/Cairo",
            "badgewell.com",
            "HOST",
            "ONLINE");
    {
        setUp(scn
                .injectOpen(rampUsers(50)
                        .during(Duration.ofSeconds(30))))
                .protocols(HttpConfig.baseConfig())
                .assertions(global().successfulRequests().percent().gt(95.0),
                        global().responseTime().percentile3().lt(5000));
    }
}
