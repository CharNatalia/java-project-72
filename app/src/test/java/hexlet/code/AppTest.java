package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppTest {
    private Javalin app;
    private MockWebServer mockServer;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        mockServer = new MockWebServer();
        mockServer.start();
        app = App.getApp();
        UrlCheckRepository.removeAll();
        UrlRepository.removeAll();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Бесплатно проверяйте сайты на SEO пригодность");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            UrlRepository.save(new Url("https://www.google.com"));
            UrlRepository.save(new Url("https://www.test.com"));
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.google.com", "https://www.test.com");
        });
    }

    @Test
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://www.test.com");
            UrlRepository.save(url);
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.test.com");
        });
    }

    @Test
    public void testUrlNotFound() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https%3A%2F%2Fwww.test.com";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string())
                    .contains("https://www.test.com");
        });
    }

    @Test
    public void testIncorrectUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "Incorrect url";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(422);
            assertThat(response.body().string())
                    .contains("Некорректный URL");
        });
    }

    @Test
    public void testIncorrectUrl2() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://www.incorrectUrlTest.incorrect");
            UrlRepository.save(url);
            var response = client.post("/urls/" + url.getId() + "/checks");
            assertThat(response.code()).isEqualTo(422);
            assertThat(response.body().string())
                    .contains("Некорректный адрес");
        });
    }

    @Test
    public void testUrlCheck() {
        MockResponse mockedResponse = new MockResponse()
                .setBody(readFile("testPage.html"));
        mockServer.enqueue(mockedResponse);
        HttpUrl baseUrl = mockServer.url("/test1/url/");

        JavalinTest.test(app, (server, client) -> {
            var url = new Url(baseUrl.toString());
            UrlRepository.save(url);
            var response = client.post("/urls/" + url.getId() + "/checks");
            assertThat(response.body().string())
                    .contains("200", "Test title", "Test h1", "my test description");
        });
    }

    @AfterEach
    public void afterAll() throws IOException {
        mockServer.shutdown();
    }

    String readFile(String fileName) {
        try {
            return Files.readString(
                    Paths.get(
                            getClass()
                                    .getClassLoader()
                                    .getResource(fileName)
                                    .toURI()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
