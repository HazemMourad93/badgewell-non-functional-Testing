package io.gatling.tut;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import example.HelperClassTUT.*;

import java.time.Duration;

import static example.HelperClassTUT.VideoGameRequests.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class VIDEODBRESPCODE extends  Simulation{



    ScenarioBuilder scn = scenario("Video Game Performance Test")
            .exec(VideoGameFlows.getAllGamesFlow())
            .exec(VideoGameFlows.getGameByIdFlow(1))
            .exec(VideoGameFlows.getAllGamesBasicFlow());

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(HttpConfig.baseConfig());
    }


}
