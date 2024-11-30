package Domain.server;

import Domain.config.ServiceLocator;
//import Domain.controller.CanjeController.ProductosController;
import Domain.controller.AdministrarController;
import Domain.controller.CanjeController.CanjeController;
import Domain.controller.CanjeController.ProductosController;
import Domain.controller.ColaboracionController.FormaColaboracionHController;
import Domain.controller.ColaboracionController.FormaColaboracionJController;
import Domain.controller.ColaboradorController.ColaboradorHumanoController;
import Domain.controller.ColaboradorController.ColaboradorJuridicoController;
import Domain.controller.HeladeraController.*;
import Domain.controller.HeladeraController.IncidenteController;
import Domain.controller.HeladeraController.SolicitudAperturaController;
import Domain.controller.LogInController.LogInController;
import Domain.controller.MigradorController.MigradorController;
import Domain.controller.ReporteController;
import Domain.controller.SuscripcionColaboradorController.SuscripcionColaboradorController;
import Domain.controller.TecnicoController.TecnicoController;
import Domain.controller.UsuarioController.MiPerfilController;
import Domain.controller.UsuarioController.UsuarioController;
import Domain.models.entities.reporte.estrategiasExportacion.GeneradorDeReportesSemanalmenteCron;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class Router {
    public static void init(Javalin app) {
        app.get("/inicio", ServiceLocator.instanceOf(UsuarioController.class)::show);
        app.get("/", ServiceLocator.instanceOf(UsuarioController.class)::show);


        app.get("/registrarTecnico", ServiceLocator.instanceOf(TecnicoController.class)::createRegistrarTecnico);


        app.get("/tecnico2", ServiceLocator.instanceOf(TecnicoController.class)::createRegistrarTecnico);
        app.post("/tecnico2", ServiceLocator.instanceOf(TecnicoController.class)::saveTecnico);



        app.get("/puntos", ServiceLocator.instanceOf(ProductosController.class)::index);
        /*  app.get("/confirmarCanje", ServiceLocator.instanceOf(CanjeController.class)::create);*/
        app.post("/puntos", ServiceLocator.instanceOf(CanjeController.class)::save);
        //app.post("/puntos/canjear", ServiceLocator.instanceOf(CanjeController.class)::save);

        app.get("/reportarIncidente", ServiceLocator.instanceOf(IncidenteController.class)::create);
        /* app.get("/reporte/:id", IncidenteController.class::show);
        app.post("/reporte", incidenteController::create);*/
        app.post("/reportarIncidente", ServiceLocator.instanceOf(IncidenteController.class)::save);
        app.get("/success", ctx -> { ctx.result("¡Operacion exitosa!"); });


        app.get("/registroPersonaJuridica", ServiceLocator.instanceOf(ColaboradorJuridicoController.class)::create);
        app.post("/registroPersonaJuridica", ServiceLocator.instanceOf(ColaboradorJuridicoController.class)::save);

        app.get("/registroPersonaHumana", ServiceLocator.instanceOf(ColaboradorHumanoController.class)::create);
        app.post("/registroPersonaHumana", ServiceLocator.instanceOf(ColaboradorHumanoController.class)::save);

        app.get("/personaHumana", ServiceLocator.instanceOf(ColaboradorHumanoController.class)::createHumana);
        app.post("/personaHumana", ServiceLocator.instanceOf(ColaboradorHumanoController.class)::save);

        app.get("/inicioSesion",ServiceLocator.instanceOf(LogInController.class)::index);
        app.post("/inicioSesion",ServiceLocator.instanceOf(LogInController.class)::validar);

        //app.get("/suscripcionColaborador", ServiceLocator.instanceOf(SuscripcionColaboradorController.class)::create);
        app.get("/suscripcionColaborador", ServiceLocator.instanceOf(SuscripcionColaboradorController.class)::index);
        app.post("/suscripcionColaborador", ServiceLocator.instanceOf(SuscripcionColaboradorController.class)::save);

        app.get("/perfil", ServiceLocator.instanceOf(MiPerfilController.class)::index);
        app.get("/perfil/editar", ServiceLocator.instanceOf(MiPerfilController.class)::edit);
        app.post("/perfil", ServiceLocator.instanceOf(MiPerfilController.class)::update);

        app.get("/heladera", ServiceLocator.instanceOf(HeladeraController.class)::show);

        app.get("/registroApertura", ServiceLocator.instanceOf(SolicitudAperturaController.class)::index);
        app.post("/registroApertura", ServiceLocator.instanceOf(SolicitudAperturaController.class)::save);

        app.get("/colaboraciones", ServiceLocator.instanceOf(FormaColaboracionHController.class)::create);
        app.post("/colaboraciones", ServiceLocator.instanceOf(FormaColaboracionHController.class)::save);

        app.get("/colaboracionSuccess", ServiceLocator.instanceOf(FormaColaboracionHController.class)::show);
        app.get("/canjeSuccess", ServiceLocator.instanceOf(CanjeController.class)::show);

        app.get("/cargar_csv", ServiceLocator.instanceOf(MigradorController.class)::show);
        app.post("/cargar_csv", ServiceLocator.instanceOf(MigradorController.class)::save);

        app.get("/administrador", ServiceLocator.instanceOf(AdministrarController.class)::create);
        app.get("/personaJuridica", ServiceLocator.instanceOf(FormaColaboracionJController.class)::show);
        app.get("/visualizarHeladeras", ServiceLocator.instanceOf(AdministrarController.class)::createVisualizarHeladeras);
        app.get("/visualizarOfertas", ServiceLocator.instanceOf(AdministrarController.class)::createVisualizarOfertas);
        app.get("/visualizarIncidentes", ServiceLocator.instanceOf(AdministrarController.class)::createVisualizarIncidentes);
        app.get("/visualizarCanjes", ServiceLocator.instanceOf(CanjeController.class)::createVisualizarCanjes);

        app.get("/gestionarFormularios", ServiceLocator.instanceOf(AdministrarController.class)::createVisualizarFormularios);

        app.get("/gestionarUsuarios", ServiceLocator.instanceOf(AdministrarController.class)::createGestionarUsuario);
        app.post("/gestionarUsuarios", ServiceLocator.instanceOf(AdministrarController.class)::deleteUsuario);


        app.get("/visualizarReportes", ServiceLocator.instanceOf(ReporteController.class)::createVisualizarReportes);
        app.get("/cerrarSesion", ServiceLocator.instanceOf(LogInController.class)::logout);

        app.get("/visitaTecnica", ServiceLocator.instanceOf(TecnicoController.class)::create);
        app.post("/visitaTecnica", ServiceLocator.instanceOf(TecnicoController.class)::save);


        app.post("/registrarTecnico", ServiceLocator.instanceOf(TecnicoController.class)::saveTecnico);

        app.get("/visualizarVisitasTecnicas",ServiceLocator.instanceOf(AdministrarController.class)::createVisualizarVisitasTecnicas);

        app.get("/preguntasFrecuentes",ServiceLocator.instanceOf(AdministrarController.class)::createPreguntasFrecuentes);

        app.get("/termino",ServiceLocator.instanceOf(AdministrarController.class)::createTermino);

        app.get("/contacto",ServiceLocator.instanceOf(AdministrarController.class)::createContacto);


        app.post("/generar-reporte", ctx -> {
            GeneradorDeReportesSemanalmenteCron generador = new GeneradorDeReportesSemanalmenteCron();
            generador.run();
            ctx.result("Reporte generado exitosamente.");
        });
    }

}
