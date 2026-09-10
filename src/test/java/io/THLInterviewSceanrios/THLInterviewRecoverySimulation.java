package io.THLInterviewSceanrios;

import example.HelperClassTUT.ConfigReader;
import example.THLAPIs.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf12RecoveryAndDataIntegrity;
import static io.gatling.javaapi.core.CoreDsl.*;

public class

THLInterviewRecoverySimulation extends Simulation {
    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");
    ScenarioBuilder scn = perf12RecoveryAndDataIntegrity(admintoken1,
            "66252f12e0313ea0b127ef7a", "6aa274cc24d854265a58e93e");
    {
        setUp(scn
                .injectOpen(rampUsers(150)
                        .during(Duration.ofMinutes(2))))
                .protocols(HttpConfig.baseConfig())
                .assertions(global().successfulRequests().percent().gt(95.0),
                        global().responseTime().percentile3().lt(7500));
    }
}
