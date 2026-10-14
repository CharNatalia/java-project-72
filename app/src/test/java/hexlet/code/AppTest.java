package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static io.javalin.util.FileUtil.readFile;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    private static Javalin app;
    private static String baseUrl;

    @BeforeAll
    public static void beforeAll() throws IOException, SQLException {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    // Тесты не зависят друг от друга
    // Но хорошей практикой будет возвращать базу данных между тестами в исходное состояние
    @BeforeEach
    void beforeEach() throws IOException, SQLException {
        runScript("truncate.sql");
        runScript("seed-test-db.sql");
    }

    private static void runScript(String fileName) throws IOException, SQLException {
        var inputStream = AppTest.class.getClassLoader().getResourceAsStream(fileName);
        String sql;
        try (var reader =
                     new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            sql = reader.lines().collect(Collectors.joining("\n"));
        }
        try (var connection = BaseRepository.dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Nested
    class RootTest {

        @Test
        void testIndex() {
            HttpResponse<String> response = Unirest.get(baseUrl + NamedRoutes.rootPath()).asString();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.getBody()).contains("Бесплатно проверяйте сайты на SEO пригодность");
        }
    }


    @Nested
    class UrlTest {

        @Test
        void testIndex() {
            HttpResponse<String> response = Unirest.get(baseUrl + NamedRoutes.urlsPath()).asString();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.getBody()).contains("https://ru.hexlet.io", "https://github.com");
        }

        @Test
        void testShow() {
            HttpResponse<String> response = Unirest.get(baseUrl + NamedRoutes.urlPath(1L)).asString();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.getBody()).contains("https://ru.hexlet.io");
        }

        @Test
        void testCreate() throws SQLException {
            String inputName = "https://www.example.com";
            HttpResponse responsePost =
                    Unirest.post(baseUrl + NamedRoutes.urlsPath())
                            .field("url", inputName)
                            .asEmpty();

            assertThat(responsePost.getStatus()).isEqualTo(HttpStatus.FOUND.getCode());


            Url actualUrl = UrlRepository.findByName(inputName).orElse(null);

            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getName()).isEqualTo(inputName);

            assertThat(responsePost.getHeaders().getFirst("Location"))
                    .isEqualTo(NamedRoutes.urlPath(actualUrl.getId()));

            HttpResponse<String> response = Unirest.get(baseUrl + NamedRoutes.urlPath(actualUrl.getId())).asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(body).contains(inputName);
            assertThat(body).contains("Страница успешно добавлена");
        }

        @Test
        void testInvalidUrl() {
            String inputName = "https://invalid url.com";
            HttpResponse<String> responsePost =
                    Unirest.post(baseUrl + NamedRoutes.urlsPath())
                            .field("url", inputName)
                            .asString();

            assertThat(responsePost.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT.getCode());
            assertThat(responsePost.getHeaders().containsKey("Location")).isFalse();

            String body = responsePost.getBody();
            assertThat(body).contains("Некорректный URL");
        }

        @Test
        void testUrlAlreadyExist() throws SQLException {
            String inputName = "https://www.example.com";
            Unirest.post(baseUrl + NamedRoutes.urlsPath())
                    .field("url", inputName)
                    .asEmpty();

            int countBefore = UrlRepository.getEntities().size();

            Url actualUrl = UrlRepository.findByName(inputName).orElse(null);

            assertThat(actualUrl).isNotNull();

            HttpResponse responsePost2 =
                    Unirest.post(baseUrl + NamedRoutes.urlsPath())
                            .field("url", actualUrl.getName())
                            .asEmpty();

            assertThat(UrlRepository.getEntities()).hasSize(countBefore);
            assertThat(responsePost2.getStatus()).isEqualTo(HttpStatus.FOUND.getCode());

            assertThat(responsePost2.getHeaders().getFirst("Location"))
                    .isEqualTo(NamedRoutes.urlPath(actualUrl.getId()));

            HttpResponse<String> response = Unirest.get(baseUrl + NamedRoutes.urlPath(actualUrl.getId())).asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(body).contains(inputName);
            assertThat(body).contains("Страница уже существует");
        }

        @Test
        void testShowNotFound() {
            HttpResponse<String> response = Unirest.get(baseUrl + NamedRoutes.urlPath(999L)).asString();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
        }

        @Test
        void testShowInvalidId() {
            HttpResponse<String> response = Unirest.get(baseUrl + NamedRoutes.urlPath("sss")).asString();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
        }


    }

    @Nested
    class UrlChecksTest {
        @Test
        void testChecks() throws IOException, SQLException {
            try (MockWebServer mockServer = new MockWebServer()) {
                mockServer.enqueue(new MockResponse.Builder()
                        .body(readFile("src/test/resources/testPage.html"))
                        .build());

                mockServer.start();
                var url = new Url(mockServer.url("/").toString());
                UrlRepository.save(url);

                Unirest.post(baseUrl + NamedRoutes.urlCheckPath(url.getId()))
                        .asEmpty();

                HttpResponse<String> response =
                        Unirest.get(baseUrl + NamedRoutes.urlPath(url.getId()))
                                .asString();

                var checks = UrlCheckRepository.getEntitiesByUrlId(url.getId());

                assertThat(checks).hasSize(1);

                var check = checks.getFirst();

                assertThat(check.getStatusCode()).isEqualTo(HttpStatus.OK.getCode());
                assertThat(check.getTitle()).isEqualTo("Test title");
                assertThat(check.getH1()).isEqualTo("Test h1");
                assertThat(check.getDescription()).isEqualTo("my test description");

                assertThat(response.getBody())
                        .contains("200", "Test title", "Test h1", "my test description");
            }
        }

        @Test
        void testInvalidCheck() throws IOException, SQLException {
            try (MockWebServer mockServer = new MockWebServer()) {
                mockServer.enqueue(new MockResponse.Builder()
                        .code(HttpStatus.NOT_FOUND.getCode())
                        .build());
                mockServer.start();
                var url = new Url(mockServer.url("/").toString());
                UrlRepository.save(url);
                int countBefore =
                        UrlCheckRepository.getEntitiesByUrlId(url.getId()).size();
                HttpResponse<String> failedResponse =
                        Unirest.post(baseUrl + NamedRoutes.urlCheckPath(url.getId()))
                                .asString();

                assertThat(failedResponse.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT.getCode());
                assertThat(failedResponse.getBody())
                        .contains("Произошла ошибка при проверке");

                assertThat(UrlCheckRepository.getEntitiesByUrlId(url.getId()))
                        .hasSize(countBefore);
            }
        }

    }
}

