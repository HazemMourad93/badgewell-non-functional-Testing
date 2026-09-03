package io.THLInterviewSceanrios;

import example.HelperClassTUT.ConfigReader;
import example.THLAPIs.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.candidateJoinScenario;
import static io.gatling.javaapi.core.CoreDsl.*;

/** Candidate-authenticated join traffic, isolated from all admin scenarios. */
public class THLInterviewJoinSimulation extends Simulation {
    String candtoken = ConfigReader.properties.getProperty("candtoken");
    ScenarioBuilder scn = candidateJoinScenario(candtoken,
            "66252f12e0313ea0b127ef7a", "6a89d0315d19d0f6c3182562");
    {
        setUp(scn
                .injectOpen(
                        rampUsers(20)
                                .during(Duration.ofSeconds(20)),
                        stressPeakUsers(500)
                                .during(Duration.ofSeconds(30))))
                .protocols(HttpConfig.baseConfig())
                .assertions(global().successfulRequests().percent().gt(90.0),
                        global().responseTime().percentile3().lt(10000));
    }
}
