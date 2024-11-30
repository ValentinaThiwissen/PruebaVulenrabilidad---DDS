package Domain.controller.UsuarioController;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.reporte.exportable.Exportable;
import Domain.models.entities.reporte.exportador.Exportador;
import Domain.models.entities.reporte.reportes.*;
import Domain.models.repository.ColaboradorRepository;
import Domain.models.repository.HeladeraRepository;
import Domain.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReporteController implements ICrudViewsHandler, WithSimplePersistenceUnit {
    private GeneradorReportesViandasPorColaborador generadorReportesViandasPorColaborador;
    private GeneradorReportesViandas generadorReportesViandas;
    private GeneradorReportesFallas generadorReportesFallas;
    private HeladeraRepository heladeraRepository;
    private ColaboradorRepository colaboradorRepository;

    public ReporteController(
            GeneradorReportesFallas generadorReportesFallas,
            GeneradorReportesViandas generadorReportesViandas,
            GeneradorReportesViandasPorColaborador generadorReportesViandasPorColaborador,
            HeladeraRepository heladeraRepository,
            ColaboradorRepository colaboradorRepository) {
        this.generadorReportesFallas = generadorReportesFallas;
        this.generadorReportesViandas = generadorReportesViandas;
        this.generadorReportesViandasPorColaborador = generadorReportesViandasPorColaborador;
        this.heladeraRepository = heladeraRepository;
        this.colaboradorRepository= colaboradorRepository;
    }

    public ReporteController() {

    }

    public void mostrarFormularioReportes(Context ctx) {
        ctx.render("visualizacionReportes.hbs"); // Archivo HBS que mostrará el formulario para seleccionar el tipo de reporte
    }

    public void generarReporte(Context ctx) {
        String tipoReporte = ctx.formParam("tipoReporte"); // Tipo del reporte seleccionado
        List<Heladera> heladeras = this.heladeraRepository.buscarTodos(); // Metodo para obtener heladeras (puedes conectarlo a tu repositorio)
        List<Colaborador> colaboradores1 = this.colaboradorRepository.buscarTodos();// Metodo para obtener colaboradores
        HashSet<Colaborador> colaboradores = new HashSet<>(colaboradores1);
        ReporteExportable reporte;
        if ("fallas".equalsIgnoreCase(tipoReporte)) {
            reporte = generadorReportesFallas.generarReporte((Set<Heladera>) heladeras, colaboradores);
        } else if ("viandas".equalsIgnoreCase(tipoReporte)) {
            reporte = generadorReportesViandasPorColaborador.generarReporte((Set<Heladera>) heladeras, colaboradores);

        }else if("viandasPorColaborador".equalsIgnoreCase(tipoReporte)){
            reporte = generadorReportesViandas.generarReporte((Set<Heladera>) heladeras, colaboradores);

        }else {
            ctx.status(400).result("Tipo de reporte no válido");
            return;
        }

        ctx.sessionAttribute("reporteGenerado", reporte); // Se supone que se guarda el reporte para luego exportar
        ctx.render("reporte_generado.hbs", reporte.getDatos());
    }
    public void exportarReporte(Context ctx) throws IOException {
        ReporteExportable reporte = ctx.sessionAttribute("reporteGenerado");//aca matechead con lo de arrib


        if (reporte == null) {
            ctx.status(404).result("No se encontró un reporte generado previamente");
            return;
        }

        String tipoReporte = ctx.formParam("tipoReporte"); // Tipo del reporte seleccionado para exportación
        Exportador exportador = null;

        if ("fallas".equalsIgnoreCase(tipoReporte)) {
            exportador = generadorReportesFallas.exportador();
        } else if ("viandas".equalsIgnoreCase(tipoReporte)) {
            exportador = generadorReportesViandas.exportador();
        }else if("viandasPorColaborador".equalsIgnoreCase(tipoReporte)){
            exportador = generadorReportesViandasPorColaborador.exportador();
        }

        if (exportador != null) {
            String rutaPDF = exportador.exportar(reporte);

            // Enviar el archivo PDF como respuesta
            ctx.header("Content-Disposition", "attachment; filename=" + rutaPDF);
            ctx.contentType("application/pdf");
            ctx.result(Files.readAllBytes(Paths.get(rutaPDF))); // Lee el archivo PDF para enviarlo
        } else {
            ctx.status(400).result("Error al exportar el reporte");
        }
    }

    @Override
    public void index(Context context) {

    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }
}
