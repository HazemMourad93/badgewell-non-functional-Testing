package io.THLInterviewSceanrios;

import example.HelperClassTUT.ConfigReader;
import example.THLAPIs.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf09Stress;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewStressSimulation extends Simulation {
    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");
    ScenarioBuilder scn = perf09Stress(admintoken1, "66252f12e0313ea0b127ef7a",
            "6a89d0315d19d0f6c3182562");
    {
        setUp(scn
                .injectOpen(stressPeakUsers(1500)
                        .during(Duration.ofMinutes(7))))
                .protocols(HttpConfig.baseConfig())
                .assertions(global().successfulRequests().percent().gt(85.0),
                        global().responseTime().percentile3().lt(15000));
    }
}
