package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlChecksController;
import hexlet.code.controller.UrlsController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public class App {
    private static final Path TEMPLATES_PATH = Path.of("src", "main", "resources", "templates");
    private static final Path STATIC_PATH = Path.of("src", "main", "resources", "static");
    private static final Path JTE_CLASSES_PATH = Path.of("jte-classes");

    private static String getMode() {
        return System.getenv().getOrDefault("APP_ENV", "production");
    }

    private static boolean isDevelopment() {
        return getMode().equals("development") && Files.isDirectory(TEMPLATES_PATH);
    }

    private static boolean isProduction() {
        return getMode().equals("production");
    }

    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(getPort());
    }

    public static Integer getPort() {
        return Integer.parseInt(
                System.getenv().getOrDefault("PORT", "7070")
        );
    }

    public static Javalin getApp() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());

        var dataSource = new HikariDataSource(hikariConfig);

        // Схему и демонстрационные статьи кладёт сам код при старте: у примера нет
        // отдельного шага миграций, а пустой список статей выглядит как поломка.
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(readResourceFile("schema.sql"));
        }
        BaseRepository.dataSource = dataSource;

        // Молча выбранный режим неотличим от «правка разметки не подхватилась».
        log.info(
                "Mode: {}",
                isDevelopment()
                        ? "development, templates and static are read from src"
                        : "production, templates and static are read from classpath");
        return Javalin.create(config -> {
            if (!isProduction()) {
                config.bundledPlugins.enableDevLogging();
            }
            config.fileRenderer(new JavalinJte(createTemplateEngine()));

            if (isDevelopment()) {
                config.staticFiles.add(STATIC_PATH.toString(), Location.EXTERNAL);
            } else {
                config.staticFiles.add("/static", Location.CLASSPATH);
            }


            config.routes.get(NamedRoutes.rootPath(), RootController::index);
            config.routes.post(NamedRoutes.urlsPath(), UrlsController::create);
            config.routes.get(NamedRoutes.urlPath("{id}"), UrlsController::show);
            config.routes.get(NamedRoutes.urlsPath(), UrlsController::index);
            config.routes.post(NamedRoutes.urlCheckPath("{id}"), UrlChecksController::check);
        });
    }

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:blog");
    }

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (var reader =
                     new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private static TemplateEngine createTemplateEngine() {
        if (isDevelopment()) {
            var codeResolver = new DirectoryCodeResolver(TEMPLATES_PATH);
            return TemplateEngine.create(codeResolver, JTE_CLASSES_PATH, ContentType.Html);
        }
        var classLoader = App.class.getClassLoader();
        var codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
}
