package hexlet.code.controller;

import hexlet.code.dto.urls.BasePage;
import hexlet.code.dto.urls.BuildUrlsPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {
    public static void build(Context ctx) {
        ctx.render("urls/build.jte");
    }

    public static void create(Context ctx) {
        try {
            var name = ctx.formParamAsClass("url", String.class)
                    .check(value -> !value.isEmpty(), "Поле не должно быть пустым")
                    .get();
            URI uri = new URI(name);
            String url = uri.getScheme() + "://" + uri.getAuthority();
            if (UrlRepository.existsByName(url)) {
                ctx.sessionAttribute("flash-info", "Страница уже существует");
            } else {
                UrlRepository.save(new Url(url));
                ctx.sessionAttribute("flash-success", "Страница успешно добавлена");
            }
            ctx.redirect("/urls");
        } catch (Exception e) {
            ctx.status(422);
            ctx.render("urls/build.jte",
                    model("page",
                            new BasePage("warning", "Некорректный URL")));
        }
    }

    public static void index(Context ctx) throws SQLException {
        var page = new BuildUrlsPage(UrlRepository.getEntities());
        if (ctx.sessionAttribute("flash-success") != null) {
            page.setAlertType("success");
            page.setMessage(ctx.consumeSessionAttribute("flash-success"));
        } else if (ctx.sessionAttribute("flash-info") != null) {
            page.setAlertType("info");
            page.setMessage(ctx.consumeSessionAttribute("flash-info"));
        }

        ctx.render("urls/index.jte",
                model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Post not found"));
        var page = new UrlPage(url, UrlCheckRepository.getEntities(id));
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void urlCheck(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(urlId)
                .orElseThrow(() -> new NotFoundResponse("Post not found"));

        try {
            var response = Unirest.get(url.getName())
                    .asString();
            var status = response.getStatus();
            var body = response.getBody();
            Document document = Jsoup.parse(body);
            var title = document.title();
            var h1 = parseElement(document.selectFirst("h1"));
            var descEl = document.selectFirst("meta[name=description]");
            var description = descEl != null ? descEl.attr("content") : null;
            var urlCheck = new UrlCheck(urlId, status, h1, title, description);

            UrlCheckRepository.save(urlCheck);
            show(ctx);
        } catch (Exception e) {
            ctx.status(422);
            var page = new UrlPage(url, null);
            page.setAlertType("warning");
            page.setMessage("Некорректный адрес");
            ctx.render("urls/show.jte", model("page", page));
        }
    }

    private static String parseElement(Element element) {
        if (element == null) {
            return null;
        }
        return element.text();
    }
}
