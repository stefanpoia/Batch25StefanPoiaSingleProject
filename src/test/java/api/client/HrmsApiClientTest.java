package api.client;

import com.sun.net.httpserver.HttpServer;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.Assert.*;

public class HrmsApiClientTest {
    private HttpServer server;
    private String base;
    private final AtomicReference<String> request = new AtomicReference<>();
    private final AtomicReference<String> cookie = new AtomicReference<>();

    @Before public void start() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/web/index.php/api/v2/pim/employees", exchange -> {
            request.set(exchange.getRequestMethod() + " " + exchange.getRequestURI());
            cookie.set(exchange.getRequestHeaders().getFirst("Cookie"));
            byte[] body = "{\"data\":[{\"empNumber\":7}],\"meta\":{\"total\":1}}".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            try (var output = exchange.getResponseBody()) { output.write(body); }
        });
        server.createContext("/redirect/api/v2/pim/employees", exchange -> {
            exchange.getResponseHeaders().set("Location", base + "/api/v2/pim/employees");
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        });
        server.start();
        base = "http://127.0.0.1:" + server.getAddress().getPort() + "/web/index.php";
    }

    @After public void stop() { if (server != null) server.stop(0); }

    @Test public void collectionUsesCorrectRouteQueryAndSession() {
        Response response = new HrmsApiClient(base + "/", Map.of("orangehrm", "test-session"), 2000).employees(2, 3);
        assertEquals(200, response.statusCode());
        assertTrue(request.get().startsWith("GET /web/index.php/api/v2/pim/employees?"));
        assertTrue(request.get().contains("limit=2"));
        assertTrue(request.get().contains("offset=3"));
        assertTrue(cookie.get().contains("orangehrm=test-session"));
        assertEquals(7, response.jsonPath().getInt("data[0].empNumber"));
    }

    @Test public void sessionsAreIsolatedAndDetailUsesInternalEmployeeNumber() {
        new HrmsApiClient(base, Map.of("orangehrm", "test-session"), 2000).employees(1, 0);
        new HrmsApiClient(base, Map.of(), 2000).employee(7);
        assertEquals("GET /web/index.php/api/v2/pim/employees/7/personal-details", request.get());
        assertNull(cookie.get());
    }

    @Test public void redirectsAreNotHiddenOrFollowed() {
        String redirectBase = base.replace("/web/index.php", "/redirect");
        assertEquals(302, new HrmsApiClient(redirectBase, Map.of(), 2000).employees(1, 0).statusCode());
        assertNull(request.get());
    }

    @Test(expected = IllegalArgumentException.class) public void invalidEmployeeNumberIsRejected() {
        new HrmsApiClient(base, Map.of(), 2000).employee(0);
    }

    @Test(expected = IllegalArgumentException.class) public void invalidTimeoutIsRejected() {
        new HrmsApiClient(base, Map.of(), 0);
    }
}
