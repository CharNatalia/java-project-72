package hexlet.code.controller;

import io.javalin.http.Context;

public final class RootController {
    public static void index(Context ctx) {
        ctx.render("index.jte");
    }
}
