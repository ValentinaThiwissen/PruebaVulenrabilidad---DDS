package Domain.models.entities.reporte.estrategiasExportacion;

import Domain.models.entities.reporte.exportable.Exportable;

public interface EstrategiaExportacion {
    public String exportar(Exportable exportable);

}
