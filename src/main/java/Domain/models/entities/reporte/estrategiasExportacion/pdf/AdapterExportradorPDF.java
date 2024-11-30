package Domain.models.entities.reporte.estrategiasExportacion.pdf;

import Domain.models.entities.reporte.exportable.Exportable;
import Domain.models.entities.reporte.reportes.ReporteExportable;

public interface AdapterExportradorPDF  {
    public String generarReporte(Exportable exportable);
}
