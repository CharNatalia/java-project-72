package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.helper.ValidationException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.SQLException;
import java.util.Map;


public class UrlChecksController {
    public static void check(Context ctx) throws SQLException {
        var urlId = parseId(ctx.pathParam("id"));
        Url url = UrlRepository.find(urlId)
                .orElseThrow(() -> new NotFoundResponse("Post not found"));
        try {
            HttpResponse<String> response =
                    Unirest.get(url.getName())
                            .asString();
            var status = response.getStatus();
            if (status >= 400) {
                throw new ValidationException("Произошла ошибка при проверке");
            }
            var body = response.getBody();

            Document document = Jsoup.parse(body);
            var title = document.title();
            var h1 = parseElement(document.selectFirst("h1"));
            var descEl = document.selectFirst("meta[name=description]");
            var description = descEl != null ? descEl.attr("content") : null;
            var urlCheck = new UrlCheck(urlId, status, h1, title, description);

            UrlCheckRepository.save(urlCheck);

            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");

            ctx.redirect(NamedRoutes.urlPath(urlId));
        } catch (UnirestException | ValidationException e) {
            ctx.status(422);
            ctx.sessionAttribute("flash", "Произошла ошибка при проверке");
            ctx.sessionAttribute("flash-type", "danger");
            var page = new UrlPage(url, UrlCheckRepository.getEntitiesByUrlId(url.getId()));
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
            ctx.render("urls/show.jte", Map.of("page", page));
        }
    }

    private static String parseElement(Element element) {
        if (element == null) {
            return null;
        }
        return element.text();
    }

    private static long parseId(String raw) {
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e) {
            throw new NotFoundResponse();
        }
    }
}
