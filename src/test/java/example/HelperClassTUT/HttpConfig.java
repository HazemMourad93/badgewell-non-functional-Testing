package example.HelperClassTUT;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class HttpConfig {

    public static HttpProtocolBuilder baseConfig() {
        return http
                .baseUrl("https://videogamedb.uk/api")
                .acceptHeader("application/json");
    }
}
