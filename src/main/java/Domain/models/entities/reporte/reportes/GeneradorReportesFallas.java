package Domain.models.entities.reporte.reportes;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.Incidente;
import Domain.models.entities.reporte.estrategiasExportacion.pdf.EstrategiaExportacionPDF;
import Domain.models.entities.reporte.exportador.Exportador;
import Domain.models.repository.IncidenteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class GeneradorReportesFallas implements GeneradorDeReportes{
    public EstrategiaExportacionPDF estrategiaExportacion;
    private IncidenteRepository incidenteRepository = new IncidenteRepository();



    public  GeneradorReportesFallas(EstrategiaExportacionPDF estrategiaExportacion){
        this.estrategiaExportacion = estrategiaExportacion;
    }

    @Override
    public ReporteExportable generarReporte(Set<Heladera> heladeras, Set<Colaborador> colaboradores) {
        ReporteExportable reporte = new ReporteExportable();
        System.out.println("Generando reporte");
        for (Heladera heladera : heladeras) {
            List<Incidente> incidentes =  incidenteRepository.buscarPorHeladeraId(heladera.getId());
            if(incidentes.isEmpty()){
                System.out.println("No hay incidentes para el id " + heladera.getId() );
            }
            Integer cantidadFallas = incidentes.size();
            //Integer cantidadFallas = heladera.getHistorialIncidentes().size();
            reporte.agregarDatos(heladera.getNombreHeladera(), cantidadFallas.toString());
        }
        reporte.setNombre("FallasPorHeladera");
        reporte.setTitulo("Fallas Por Heladera");
        reporte.setFechaCreacion(LocalDate.now().toString());
        return reporte;
    }

    public Exportador exportador()
    {
        Exportador exportador2 = new Exportador(estrategiaExportacion);
        return exportador2;
    }
}
