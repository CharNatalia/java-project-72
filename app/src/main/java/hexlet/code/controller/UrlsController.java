package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;

public final class UrlsController {
    public static void create(Context ctx) throws SQLException {
        try {
            String name = ctx.formParam("url");
            name = name == null ? "" : name.trim();
            URI uri = new URI(name);
            URL fullUrl = uri.toURL();
            String correctUrl = fullUrl.getProtocol() + "://" + fullUrl.getHost();
            if (fullUrl.getPort() != -1) {
                correctUrl += ":" + fullUrl.getPort();
            }
            Url url = new Url(correctUrl);
            if (UrlRepository.existsByName(correctUrl)) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flash-type", "danger");
                url = UrlRepository.findByName(correctUrl).get();
            } else {
                UrlRepository.save(url);
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.sessionAttribute("flash-type", "success");
            }
            var id = url.getId();
            ctx.redirect(NamedRoutes.urlPath(id));
        } catch (java.net.URISyntaxException | java.net.MalformedURLException | IllegalArgumentException e) {
            ctx.status(422);
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            BasePage page = new BasePage();
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
            ctx.render("index.jte", Map.of("page", page));
        }
    }

    public static void show(Context ctx) throws SQLException {
        var id = parseId(ctx.pathParam("id"));
        Url url = UrlRepository.find(id).orElseThrow(() -> new NotFoundResponse("Post not found"));
        var page = new UrlPage(url, UrlCheckRepository.getEntitiesByUrlId(url.getId()));
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/show.jte", Map.of("page", page));
    }

    public static void index(Context ctx) throws SQLException {

        var page = new UrlsPage(UrlCheckRepository.getLastChecks());
        ctx.render("urls/index.jte", Map.of("page", page));
    }

    private static long parseId(String raw) {
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e) {
            throw new NotFoundResponse();
        }
    }
}
