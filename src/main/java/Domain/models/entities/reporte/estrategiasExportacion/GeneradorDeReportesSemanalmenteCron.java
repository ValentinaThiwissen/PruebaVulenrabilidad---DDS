package Domain.models.entities.reporte.estrategiasExportacion;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.formasDeColaborar.DonacionViandas;
import Domain.models.entities.formasDeColaborar.FormaDeColaboracion;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.reporte.estrategiasExportacion.pdf.AdapterApachePDFBox;
import Domain.models.entities.reporte.estrategiasExportacion.pdf.EstrategiaExportacionPDF;
import Domain.models.entities.reporte.reportes.*;
import Domain.models.entities.vianda.Vianda;
import Domain.models.repository.ColaboradorRepository;
import Domain.models.repository.HeladeraRepository;
import Domain.models.repository.ReporteRepository;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDate;
import java.util.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GeneradorDeReportesSemanalmenteCron implements Runnable, Job {

    private final ColaboradorRepository colaboradorRepository;
    private final HeladeraRepository heladerasRepository;
    private final ScheduledExecutorService scheduler;

    public GeneradorDeReportesSemanalmenteCron() {
        // Inicializar repositorios solo una vez
        this.colaboradorRepository = new ColaboradorRepository();
        this.heladerasRepository = new HeladeraRepository();

        // Usar ScheduledExecutorService para programar la tarea periódica
        this.scheduler = Executors.newScheduledThreadPool(1); // Usamos un solo hilo
    }

    public void start() {
        // Programar la tarea para que se ejecute cada semana
        long unaSemanaEnMilisegundos = 7 * 24 * 60 * 60 * 1000;
        scheduler.scheduleAtFixedRate(this, 0, unaSemanaEnMilisegundos, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        try {
            // Crear los generadores de reportes
            GeneradorReportesFallas generadorDeReportesDeFallas = new GeneradorReportesFallas(new EstrategiaExportacionPDF(new AdapterApachePDFBox()));
            GeneradorReportesViandas generadorDeReportesDeViandas = new GeneradorReportesViandas(new EstrategiaExportacionPDF(new AdapterApachePDFBox()));
            GeneradorReportesViandasPorColaborador generadorDeReportesDeViandasPorColaborador = new GeneradorReportesViandasPorColaborador(new EstrategiaExportacionPDF(new AdapterApachePDFBox()));

            // Obtener datos de repositorios
            Set<Heladera> heladeras = new HashSet<>(heladerasRepository.buscarTodos());
            Set<Colaborador> colaboradores = new HashSet<>(colaboradorRepository.buscarTodos());
            if(heladeras.isEmpty()) {
                System.out.println("No hay heladeras");
            }
            if(colaboradores.isEmpty()) {
                System.out.println("No hay colaboradores");
            }

            // Generar reportes
            ReporteExportable reporteFallasHeladera = generadorDeReportesDeFallas.generarReporte(heladeras, colaboradores);
            ReporteExportable reporteMovimientoViandas = generadorDeReportesDeViandas.generarReporte(heladeras, colaboradores);
            ReporteExportable reporteViandasDonadas = generadorDeReportesDeViandasPorColaborador.generarReporte(heladeras, colaboradores);


            // Exportar reportes
            String rutaReporteFallasHeladera = generadorDeReportesDeFallas.exportador().exportar(reporteFallasHeladera);
            String rutaReporteMovimientoViandas = generadorDeReportesDeViandas.exportador().exportar(reporteMovimientoViandas);
            String rutaReporteViandasDonadas = generadorDeReportesDeViandasPorColaborador.exportador().exportar(reporteViandasDonadas);



            // Actualizar rutas
            reporteFallasHeladera.modificarRuta(rutaReporteFallasHeladera);
            reporteMovimientoViandas.modificarRuta(rutaReporteMovimientoViandas);
            reporteViandasDonadas.modificarRuta(rutaReporteViandasDonadas);

            // Guardar reportes en el repositorio
            ReporteRepository reporteRepository = new ReporteRepository();
            reporteRepository.guardarReporte(reporteFallasHeladera);
            reporteRepository.guardarReporte(reporteMovimientoViandas);
            reporteRepository.guardarReporte(reporteViandasDonadas);

        } catch (Exception e) {
            e.printStackTrace();
            // Manejar excepciones apropiadamente
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.run();
    }
}


