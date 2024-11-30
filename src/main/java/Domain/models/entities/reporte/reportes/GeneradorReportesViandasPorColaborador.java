package Domain.models.entities.reporte.reportes;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.reporte.estrategiasExportacion.pdf.EstrategiaExportacionPDF;
import Domain.models.entities.reporte.exportador.Exportador;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class GeneradorReportesViandasPorColaborador implements GeneradorDeReportes{
    public EstrategiaExportacionPDF estrategiaExportacion;


    public GeneradorReportesViandasPorColaborador(EstrategiaExportacionPDF unaEstrategia) {
        this.estrategiaExportacion = unaEstrategia;
    }
    public ReporteExportable generarReporte(Set<Heladera> heladeras, Set<Colaborador> colaboradores) {
        ReporteExportable reporte = new ReporteExportable();

        for (Colaborador colaborador : colaboradores) {
            Integer cantidadViandasPorColaborador = colaborador.historialViandasDonadas();
            reporte.agregarDatos(colaborador.getNombre(), cantidadViandasPorColaborador.toString());
        }

        reporte.setNombre("ViandasDonadasPorColaborador");
        reporte.setTitulo("Viandas Por Colaborador");
        reporte.setFechaCreacion(LocalDate.now().toString());

        return reporte;
    }

    public Exportador exportador()
    {
        Exportador exportador = new Exportador(estrategiaExportacion);
        return exportador;
    }
}
