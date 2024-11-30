package Domain.Middlewares;

import Domain.models.entities.Usuario.TipoRol;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Domain.Exceptions.AccesDeniedException;

public class AuthMiddleware {

    public static void apply(Javalin app) {
        app.beforeMatched(ctx -> {
            var userRole = getUserRoleType(ctx);
            if (!ctx.routeRoles().isEmpty() && !ctx.routeRoles().contains(userRole)) {
                throw new AccesDeniedException();
            }
        });
    }

    private static TipoRol getUserRoleType(Context context) {
        return context.sessionAttribute("tipo_rol") != null?
                TipoRol.valueOf(context.sessionAttribute("tipo_rol")) : null;
    }
}
