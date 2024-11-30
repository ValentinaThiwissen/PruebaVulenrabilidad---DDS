package Domain.models.entities.reporte.estrategiasExportacion.pdf;

import Domain.models.entities.reporte.estrategiasExportacion.EstrategiaExportacion;
import Domain.models.entities.reporte.exportable.Exportable;
import Domain.models.entities.reporte.reportes.ReporteExportable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstrategiaExportacionPDF implements EstrategiaExportacion {
    public  AdapterExportradorPDF adapter;
    public EstrategiaExportacionPDF(AdapterExportradorPDF adapter){
        this.adapter= adapter;
    }
    @Override
    public String exportar(Exportable exportable) {
        String rutaCompleta = adapter.generarReporte(exportable);
        return rutaCompleta;
    }
}
