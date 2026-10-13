package api.client;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import java.net.URI;
import java.util.Map;

/** Scenario-local, read-only client. Never logs session cookies or employee payloads. */
public final class HrmsApiClient {
    private final String baseUrl;
    private final Map<String, String> cookies;
    private final int timeout;

    public HrmsApiClient(String baseUrl, Map<String, String> cookies, int timeout) {
        URI uri = URI.create(baseUrl);
        if (uri.getHost() == null || !("https".equals(uri.getScheme()) || "http".equals(uri.getScheme()))
                || uri.getUserInfo() != null || uri.getQuery() != null || uri.getFragment() != null) {
            throw new IllegalArgumentException("Invalid API base URL");
        }
        if (timeout <= 0) throw new IllegalArgumentException("API timeout must be positive");
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.cookies = Map.copyOf(cookies);
        this.timeout = timeout;
    }

    public Response employees(int limit, int offset) {
        return get("/api/v2/pim/employees", Map.of("limit", limit, "offset", offset));
    }

    public Response employee(int empNumber) {
        if (empNumber <= 0) throw new IllegalArgumentException("Employee number must be positive");
        return get("/api/v2/pim/employees/" + empNumber + "/personal-details", Map.of());
    }

    private Response get(String path, Map<String, ?> parameters) {
        return RestAssured.given()
                .config(RestAssuredConfig.config()
                        .redirect(RedirectConfig.redirectConfig().followRedirects(false))
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", timeout)
                                .setParam("http.socket.timeout", timeout)
                                .setParam("http.connection-manager.timeout", (long) timeout)))
                .baseUri(baseUrl).accept("application/json").cookies(cookies)
                .queryParams(parameters).get(path);
    }
}
