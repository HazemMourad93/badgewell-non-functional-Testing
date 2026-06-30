//package io.gatling.tut;
//
//import java.time.Duration;
//import java.util.*;
//
//import io.gatling.javaapi.core.*;
//import io.gatling.javaapi.http.*;
//import io.gatling.javaapi.jdbc.*;
//
//import static io.gatling.javaapi.core.CoreDsl.*;
//import static io.gatling.javaapi.http.HttpDsl.*;
//import static io.gatling.javaapi.jdbc.JdbcDsl.*;
//
//public class RecordedSimulationProxy extends Simulation {
//
//  private HttpProtocolBuilder httpProtocol = http
//    .baseUrl("https://videogamedb.uk")
//    .disableFollowRedirect()
//    .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*", ".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*", ".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*"))
//    .acceptHeader("*/*")
//    .acceptEncodingHeader("gzip, deflate, br")
//    .userAgentHeader("PostmanRuntime/7.44.1");
//
//  private Map<CharSequence, String> headers_0 = Map.of("Postman-Token", "79704d47-f2b3-48a2-93bd-126530606289");
//
//  private Map<CharSequence, String> headers_1 = Map.of("Postman-Token", "5ee07706-d7e5-4582-af5e-79096af14afe");
//
//  private Map<CharSequence, String> headers_2 = Map.ofEntries(
//    Map.entry("Content-Type", "application/json"),
//    Map.entry("Postman-Token", "47e3eacc-dfc8-451e-b7c6-019191c7e6d1")
//  );
//
//  private Map<CharSequence, String> headers_3 = Map.ofEntries(
//    Map.entry("Content-Type", "application/json"),
//    Map.entry("Postman-Token", "1c0d7c79-993b-491b-aae8-fb061c1c877d"),
//    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2NDUxNDk4NSwiZXhwIjoxNzY0NTE4NTg1fQ.tmd8JhbGCuYLDCiFwiFNFu8PHyq2TSRGEjxyONoI16Y")
//  );
//
//  private Map<CharSequence, String> headers_4 = Map.ofEntries(
//    Map.entry("Content-Type", "application/json"),
//    Map.entry("Postman-Token", "11fef200-0d66-470c-9c87-cc760b6bbd28"),
//    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2NDUxNDk4NSwiZXhwIjoxNzY0NTE4NTg1fQ.tmd8JhbGCuYLDCiFwiFNFu8PHyq2TSRGEjxyONoI16Y")
//  );
//
//  private Map<CharSequence, String> headers_5 = Map.ofEntries(
//    Map.entry("Content-Type", "application/json"),
//    Map.entry("Postman-Token", "21f6f407-2160-4439-8b33-37ac08e6ee56"),
//    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2NDUxNDk4NSwiZXhwIjoxNzY0NTE4NTg1fQ.tmd8JhbGCuYLDCiFwiFNFu8PHyq2TSRGEjxyONoI16Y")
//  );
//
//  private Map<CharSequence, String> headers_6 = Map.ofEntries(
//    Map.entry("Postman-Token", "4adce0b4-6f48-4eaa-8404-1a4b4ea2de7c"),
//    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2NDUxNDk4NSwiZXhwIjoxNzY0NTE4NTg1fQ.tmd8JhbGCuYLDCiFwiFNFu8PHyq2TSRGEjxyONoI16Y")
//  );
//
//
//  private ScenarioBuilder scn = scenario("RecordedSimulationProxy")
//    .exec(
//      http("request_0")
//        .get("/api/videogame")
//        .headers(headers_0),
//      pause(3),
//      http("request_1")
//        .get("/api/videogame/2")
//        .headers(headers_1),
//      pause(7),
//      http("request_2")
//        .post("/api/authenticate")
//        .headers(headers_2)
//        .body(RawFileBody("io/gatling/tut/recordedsimulationproxy/0002_request.json")),
//      pause(24),
//      http("request_3")
//        .post("/api/videogame")
//        .headers(headers_3)
//        .body(RawFileBody("io/gatling/tut/recordedsimulationproxy/0003_request.json")),
//      pause(8),
//      http("request_4")
//        .put("/api/videogame/0")
//        .headers(headers_4)
//        .body(RawFileBody("io/gatling/tut/recordedsimulationproxy/0004_request.json"))
//        .check(status().is(404)),
//      pause(4),
//      http("request_5")
//        .put("/api/videogame/2")
//        .headers(headers_5)
//        .body(RawFileBody("io/gatling/tut/recordedsimulationproxy/0005_request.json")),
//      pause(5),
//      http("request_6")
//        .delete("/api/videogame/2")
//        .headers(headers_6)
//    );
//
//  {
//	  setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
//  }
//}
