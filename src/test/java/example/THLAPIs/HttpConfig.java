package example.THLAPIs;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class HttpConfig {

    public static HttpProtocolBuilder baseConfig() {
        return http
                .baseUrl("https://talent-hub-lite-dev-server-rvq7gueufa-oa.a.run.app")
                .acceptHeader("application/json; charset=UTF-8")
                .contentTypeHeader("application/json");
    }
}
