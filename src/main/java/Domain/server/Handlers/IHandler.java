package Domain.server.Handlers;
import io.javalin.Javalin;

public interface IHandler {
    void setHandle(Javalin app);
}
