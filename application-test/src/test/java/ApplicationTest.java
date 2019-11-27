import com.db.alex.rta.codegen.tables.Requests;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;

public class ApplicationTest {
    private static final long fooAccountId = 1001;
    private static final long barAccountId = 1002;

    private static final String relativeWorkDir = "target/working/";

    private static Connection connection;

    @BeforeAll
    static void createTestDb() throws SQLException {

        connection = DriverManager.getConnection("jdbc:sqlite:" + relativeWorkDir + "rta.db");
        new setupDb(connection, SQLDialect.SQLITE).execute(new HashMap<>() {{
            put(fooAccountId, 1000d);
            put(barAccountId, 1000d);
        }});
    }

    @Test
    void TransferAndGetTest() {
        IntegrationTest(() -> {
            try {
                TransferAndGetTestImpl();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void ManyRequestsTest() {
        IntegrationTest(() -> {
            try {
                ManyRequestsTestImpl();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    void IntegrationTest(Runnable runnable) {
        Process process = null;
        try {
            process = startService();
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    private void TransferAndGetTestImpl() throws IOException, InterruptedException {
        String webserviceEndpoint = "http://localhost:8090";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest transferRequest = HttpRequest.newBuilder()
                .uri(URI.create(webserviceEndpoint + "/transfer"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(getTransferRequest(fooAccountId, barAccountId, 512)))
                .build();

        HttpResponse<String> transferResponse = client.send(transferRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getFooRequest = HttpRequest.newBuilder()
                .uri(URI.create(webserviceEndpoint + "/get"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(getGetRequest(fooAccountId)))
                .build();

        HttpResponse<String> getFooResponse = client.send(getFooRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getBarRequest = HttpRequest.newBuilder()
                .uri(URI.create(webserviceEndpoint + "/get"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(getGetRequest(barAccountId)))
                .build();

        HttpResponse<String> getBarResponse = client.send(getBarRequest, HttpResponse.BodyHandlers.ofString());

        double fooAmount = Double.parseDouble(getFooResponse.body());
        double barAmount = Double.parseDouble(getBarResponse.body());

        Assertions.assertEquals(488d, fooAmount);
        Assertions.assertEquals(1512d, barAmount);

    }

    private void ManyRequestsTestImpl() throws IOException, InterruptedException {
        String webserviceEndpoint = "http://localhost:8090";
        HttpClient client = HttpClient.newHttpClient();

        for (int i = 0; i < 50; i++) {
            HttpRequest transferRequest = HttpRequest.newBuilder()
                    .uri(URI.create(webserviceEndpoint + "/transfer"))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(getTransferRequest(fooAccountId, barAccountId, 1)))
                    .build();

            client.sendAsync(transferRequest, HttpResponse.BodyHandlers.ofString());
        }

        Thread.sleep(3000);

        DSLContext create = DSL.using(connection, SQLDialect.SQLITE);
        int count = create.fetchCount(Requests.REQUESTS);

        // Hard-Coded ring-buffer size is 16
        Assertions.assertTrue(count >= 16, "NOT all requests were saved " + count);

    }

    private Process startService() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "subject.jar")
                .directory(new File(relativeWorkDir))
                .redirectOutput(new File("out.txt"))
                .redirectError(new File("errors.txt"));
        return pb.start();
    }


    private String getTransferRequest(long from, long to, double amount) {
        return String.format("{\"sourceSystemId\":100, \"sourceAccountId\": %1$d, \"targetAccountId\": %2$d, \"amount\": %3$f}", from, to, amount);
    }

    private String getGetRequest(long from) {
        return String.format("{\"sourceSystemId\":100, \"accountId\": %1$d}", from);
    }

}
