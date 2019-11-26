import org.jooq.SQLDialect;
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

    @BeforeAll
    static void createTestDb() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:rta.db");
        new setupDb(connection, SQLDialect.SQLITE).execute(new HashMap<>() {{
            put(fooAccountId, 1000d);
            put(barAccountId, 1000d);
        }});
    }

    @Test
    void IntegrationTest() {
        Process process = null;
        try {
            process = startService();
            IntegrationTestImpl();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    private void IntegrationTestImpl() throws IOException, InterruptedException {
        String webserviceEndpoint = "http://localhost:8090";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webserviceEndpoint + "/transfer"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(getTransferRequest(fooAccountId, barAccountId, 512)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private Process startService() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "subject.jar")
                .directory(new File("target/working"))
                .redirectError(new File("errors.txt"));
        return pb.start();
    }


    private String getTransferRequest(long from, long to, double amount) {
        return String.format("{\"sourceSystemId\":100, \"sourceAccountId\": %1$d, \"targetAccountId\": %2$d, \"amount\": %3$f}", from, to, amount);
    }


}
