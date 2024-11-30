package Domain.controller;

import Domain.models.entities.MigradorDeDonantes.LectorCSV;
import Domain.models.entities.Usuario.TipoRol;
import Domain.models.entities.Usuario.Usuario;
import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.reporte.reportes.ReporteExportable;
import Domain.models.repository.ReporteRepository;
import Domain.models.repository.UsuarioRepository;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import io.javalin.http.Context;

public class ReporteController implements ICrudViewsHandler, WithSimplePersistenceUnit {

    ReporteRepository reporteRepository;
    UsuarioRepository usuarioRepository;


    public ReporteController(ReporteRepository repositorioReporte, UsuarioRepository usuarioRepository) {

        this.reporteRepository = repositorioReporte;
        this.usuarioRepository = usuarioRepository;
    }

    public ReporteController() {

    }

    @Override
    public void index(Context context){
        //TODO
    }


    @Override
    public void show(Context context){
        List<ReporteExportable> reportes = reporteRepository.buscarTodos();
        context.render("visualizarReportes.hbs", Map.of("reportes", reportes));
    }

    public void createVisualizarReportes(Context context){
        Long id = context.sessionAttribute("usuario_id");

        if (id == null) {
            context.redirect("/inicioSesion");
            return;
        }

        Usuario usuario = usuarioRepository.buscarPorID(id);
        if (usuario == null) {
            context.status(404).result("Usuario no encontrado");
            return;
        }
        if (usuario.getRol().getTipo()  == TipoRol.ADMIN | usuario.getRol().getTipo()  == TipoRol.PERSONA_JURIDICA) {
            List<ReporteExportable> reportes = reporteRepository.buscarTodos();
            if(reportes == null || reportes.isEmpty()) {
                context.status(404).result("No hay reportes disponibles");
            }
            context.render("visualizarReportes.hbs", Map.of("reportes", reportes));
        }
    }

    @Override
    public void update(Context context) {
        //TODO
    }
    @Override
    public void delete(Context context) {
        //TODO
    }
    @Override
    public void edit(Context context) {
        //TODO
    }
    @Override
    public void create(Context context) {
        //TODO
    }

    @Override
    public void save(Context context) {

    }
}