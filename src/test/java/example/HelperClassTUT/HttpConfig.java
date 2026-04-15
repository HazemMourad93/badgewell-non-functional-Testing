package example.HelperClassTUT;

import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.core.ProtocolBuilder;
import org.jspecify.annotations.NonNull;

public class HttpConfig {

    public static @NonNull ProtocolBuilder baseConfig() {
        return http
                .baseUrl("https://videogamedb.uk/api")
                .acceptHeader("application/json");
    }
}
