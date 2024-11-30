package Domain.server.Handlers;

import io.javalin.Javalin;
import kotlin.io.AccessDeniedException;

public class AccessDeniedHandler implements IHandler {

    @Override
    public void setHandle(Javalin app) {
        app.exception(AccessDeniedException.class, (e, context) -> {
            context.status(401);
            context.render("401.hbs");
        });
    }
}