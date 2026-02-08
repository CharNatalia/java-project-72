package hexlet.code.controller;

import hexlet.code.dto.urls.BuildUrlPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;

import java.net.URI;
import java.net.URL;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {
    public static void create(Context ctx) {
        try {
            var name = ctx.formParamAsClass("url", String.class)
                    .check(value -> !value.isEmpty(), "Поле не должно быть пустым")
                    .get();
            URI uri = new URI(name);
            URL url = uri.toURL();

            UrlRepository.save(new Url(url.toString()));
        } catch (Exception e) {
            ctx.status(422);
            ctx.render("urls/build.jte",
                    model("page", new BuildUrlPage(
                            Map.of("url", "Некорректный URL")
                    )));
        }
    }
}
