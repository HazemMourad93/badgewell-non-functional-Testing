package io.THLInterviewSceanrios;

import example.HelperClassTUT.ConfigReader;
import example.THLAPIs.HttpConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static example.THLAPIs.THLinterviewSceanrios.perf03ConcurrentInterviewOperations;
import static io.gatling.javaapi.core.CoreDsl.*;

public class THLInterviewConcurrencySimulation extends Simulation {
    String admintoken1 = ConfigReader.properties.getProperty("admintoken1");
    ScenarioBuilder scn = perf03ConcurrentInterviewOperations(admintoken1,
            "66252f12e0313ea0b127ef7a", "6aa2969c34028cd21149e549");
    {
        setUp(scn
                .injectOpen(rampUsers(50)
                        .during(Duration.ofSeconds(60))))
                .protocols(HttpConfig.baseConfig())
                .assertions(global().successfulRequests().percent().gt(95.0),
                        global().responseTime().percentile3().lt(5000),
                        global().successfulRequests().percent().gte(85.0),
                        global().failedRequests().percent().lt(10.0),
                        global().responseTime().percentile3().lt(4000),
                        global().responseTime().max().lt(90000));
    }
}
