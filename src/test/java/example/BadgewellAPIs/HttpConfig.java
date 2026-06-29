package example.BadgewellAPIs;

import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.jspecify.annotations.NonNull;

import static io.gatling.javaapi.http.HttpDsl.*;

public class HttpConfig {

    public static @NonNull HttpProtocolBuilder baseConfig() {

        return http
                .baseUrl("https://badgewell-crm-release-app-z667sx5a2q-ey.a.run.app")
                .acceptHeader("application/json");


    }





}