package Domain.models.entities.reporte.exportador;

import Domain.models.entities.reporte.estrategiasExportacion.EstrategiaExportacion;
import Domain.models.entities.reporte.estrategiasExportacion.pdf.EstrategiaExportacionPDF;
import Domain.models.entities.reporte.exportable.Exportable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
public class Exportador {
    private EstrategiaExportacionPDF estrategia;

    public Exportador(EstrategiaExportacionPDF estrategiaExportacion) {
        this.setEstrategia(estrategiaExportacion);
    }

    public String exportar(Exportable exportable) {
        return estrategia.exportar(exportable);
    }

}
