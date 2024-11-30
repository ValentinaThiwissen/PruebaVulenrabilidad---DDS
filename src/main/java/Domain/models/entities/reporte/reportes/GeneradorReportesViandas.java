package Domain.models.entities.reporte.reportes;

import Domain.models.entities.colaborador.Colaborador;
import Domain.models.entities.heladera.Heladera;
import Domain.models.entities.heladera.MovimientoVianda;
import Domain.models.entities.reporte.estrategiasExportacion.pdf.EstrategiaExportacionPDF;
import Domain.models.entities.reporte.exportador.Exportador;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class GeneradorReportesViandas implements GeneradorDeReportes{
    public EstrategiaExportacionPDF estrategiaExportacion;

    /*Cant de viandas retiradas/colocadas por heladera*/

    @Override
    public ReporteExportable generarReporte(Set<Heladera> heladeras, Set<Colaborador> colaboradores) {
        ReporteExportable reporte = new ReporteExportable();

        for (Heladera heladera : heladeras) {
            int viandasMovidas = 0;
            Set<MovimientoVianda> movimientos = heladera.movimientosExitososViandas();

            for(MovimientoVianda movimiento : movimientos){
                viandasMovidas += movimiento.getViandas();
            }
            reporte.agregarDatos(heladera.getNombreHeladera(), String.valueOf(viandasMovidas));
        }
        reporte.setNombre("ViandasPorHeladera");
        reporte.setTitulo("Viandas Por Heladera");
        reporte.setFechaCreacion(LocalDate.now().toString());
        return reporte;
    }

    public GeneradorReportesViandas(EstrategiaExportacionPDF unaEstrategia) {
        this.estrategiaExportacion = unaEstrategia;
    }
    public Exportador exportador()
    {
        Exportador exportador = new Exportador(estrategiaExportacion);
        return exportador;
    }
}
