package Domain.models.entities.reporte.estrategiasExportacion.pdf;

import Domain.models.entities.reporte.config.Config;
import Domain.models.entities.reporte.exportable.Exportable;
import Domain.models.entities.reporte.reportes.ReporteExportable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class AdapterApachePDFBox implements AdapterExportradorPDF {

    private String nombreDeArchivo;


    @Override
    public String generarReporte(Exportable exportable){
        this.nombreDeArchivo = nombre_archivo(exportable);
        PDDocument doc = new PDDocument();
        PDPage myPage = new PDPage();
        doc.addPage(myPage);
        try {
            PDPageContentStream cont = new PDPageContentStream(doc, myPage);
            cont.beginText();
            cont.setFont(PDType1Font.TIMES_ROMAN, 12);
            cont.setLeading(14.5f);
            cont.newLineAtOffset(25, 700);
            this.agregarDatos(cont, exportable.getDatos());

            cont.endText();
            cont.close();
            doc.save(this.rutaCompletaDelArchivo(exportable));
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.rutaCompletaDelArchivo(exportable);
    }

    private String rutaCompletaDelArchivo(Exportable exportable) {
        // Obtener la ruta absoluta en la carpeta de recursos
        String rutaBase = Paths.get("Entrega6","externals", "reportes").toString();
        // Asegúrate de que la carpeta exista
        try {
            Files.createDirectories(Paths.get(rutaBase));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Paths.get(rutaBase, this.nombreDeArchivo).toString();
    }

    public String nombre_archivo(Exportable exportable)
    {
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombre = fechaHoraActual + "_" + exportable.nombre() + ".pdf";
        exportable.setNombre(nombre);
        return nombre;
    }

    private void agregarDatos(PDPageContentStream pagina, Map<String, List<String>> datos) throws IOException {
        float y = 700;
        for (Map.Entry<String, List<String>> entry : datos.entrySet()) {
            pagina.newLineAtOffset(0, -14.5f);
            String nombre = entry.getKey();
            String cantidad = entry.getValue().get(0);
            String datosDeLaFila = nombre + "   " + cantidad + "   ";
            pagina.showText(datosDeLaFila);
        }
    }
}
