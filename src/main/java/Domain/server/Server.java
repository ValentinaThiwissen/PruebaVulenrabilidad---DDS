package Domain.server;

import Domain.config.ServiceLocator;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.reporte.estrategiasExportacion.GeneradorDeReportesSemanalmenteCron;
import Domain.models.repository.UsuarioRepository;
import Domain.utils.Initializer;
import Domain.utils.JavalinRenderer;
import Domain.utils.PrettyProperties;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.http.staticfiles.Location;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.function.Consumer;

public class Server {
    private static Javalin app = null;

    private static final String baseDir = Paths.get("").toAbsolutePath().toString();

    public static Javalin app() {
        if (app == null)
            throw new RuntimeException("App no inicializada");
        return app;
    }

    public static void init() throws SchedulerException {
        if (app == null) {
            Integer port = Integer.parseInt(PrettyProperties.getInstance().propertyFromName("server_port"));
            app = Javalin.create(config()).start(port);


            app.before(ctx -> {
                String usuarioCookie = ctx.cookie("usuario");
                if (usuarioCookie != null) {
                    Usuario usuario = ServiceLocator.instanceOf(UsuarioRepository.class).buscarPorNombreUsuario(usuarioCookie);
                    if (usuario != null) {
                        ctx.sessionAttribute("usuario_id", usuario.getId());
                    } else {
                        // Si no se encuentra el usuario, redirigir a inicio de sesión
                        ctx.redirect("/inicioSesion");
                    }
                }
            });
            GeneradorDeReportesSemanalmenteCron generador = new GeneradorDeReportesSemanalmenteCron();

            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler scheduler = sf.getScheduler();
            JobDetail jobl = JobBuilder.newJob(GeneradorDeReportesSemanalmenteCron.class).withIdentity("generadorReportesJob", "grupo04")
                    .usingJobData("Info", "Valor").build();
            Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("GeneradorTrigger", "grupo04")
                    .startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(168).repeatForever()).build();
            scheduler.scheduleJob(jobl, trigger1);
            scheduler.start();

            Router.init(app);

            Initializer.init();
            if (Boolean.parseBoolean(PrettyProperties.getInstance().propertyFromName("dev_mode"))) {
                Initializer.init();
            }
        }
    }

    private static Consumer<JavalinConfig> config() {
        return config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "public";
            });
            config.staticFiles.add(staticFiles-> {
                staticFiles.hostedPath = "/reportes";
                staticFiles.directory = Paths.get(baseDir,"Entrega6/externals/reportes").toString();
                staticFiles.location = Location.EXTERNAL;
            });
            config.staticFiles.add(staticFiles-> {
                staticFiles.hostedPath = "/visitasTecnicas";
                staticFiles.directory = Paths.get(baseDir,"Entrega6/externals/visitasTecnicas").toString();
                staticFiles.location = Location.EXTERNAL;
            });

            config.fileRenderer(new JavalinRenderer().register("hbs", (path, model, context) -> {
                Handlebars handlebars = new Handlebars();
                Template template = null;
                try {
                    template = handlebars.compile(
                            "templates/" + path.replace(".hbs", ""));
                    return template.apply(model);
                } catch (IOException e) {
                    e.printStackTrace();
                    context.status(HttpStatus.NOT_FOUND);
                    return "No se encuentra la página indicada...";
                }
            }));
        };
    }
}
