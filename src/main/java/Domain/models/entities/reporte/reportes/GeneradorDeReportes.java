package Domain.models.entities.reporte.reportes;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;

import java.util.Set;

public interface GeneradorDeReportes {
    public  ReporteExportable generarReporte(Set <Heladera>heladeras, Set<Colaborador>colaboradores);

}
