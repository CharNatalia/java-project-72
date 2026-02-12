package hexlet.code.controller;

import hexlet.code.dto.urls.BasePage;
import hexlet.code.dto.urls.BuildUrlsPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.URI;
import java.net.URL;
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
            URL url = uri.toURL();
            if (UrlRepository.existsByName(url.toString())) {
                ctx.sessionAttribute("flash-info", "Страница уже существует");
            } else {
                UrlRepository.save(new Url(url.toString()));
                ctx.sessionAttribute("flash-success", "Страница успешно создана");
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
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }
}
