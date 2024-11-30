package Domain.models.entities.reporte.reportes;

import Domain.models.entities.reporte.exportable.Exportable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Data
@Entity
@DiscriminatorValue("ReporteExportable")
public class ReporteExportable extends Exportable {

    @GeneratedValue
    @Id
    public Long id;
    @Column(columnDefinition = "varchar(255)")
    public String path;
    @Column(columnDefinition = "varchar(255)")
    public String nombre;
    @Transient
    private Map<String, List<String>> datos = new HashMap<>();
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "fecha_creacion")
    private String fechaCreacion;


    public ReporteExportable(){

    }

    public ReporteExportable(String nombre, Map<String, List<String>> datos) {
        this.nombre = nombre;
        this.datos = datos;
    }

    public void agregarDatos(String nombreHeladera, String cantidadFallas) {
        List<String> datosHeladera = new ArrayList<>();
        //datosHeladera.add(nombreHeladera);
        datosHeladera.add(cantidadFallas);
        datos.put(nombreHeladera, datosHeladera);
    }

    @Override
    public void setNombre(String nombre){this.nombre = nombre;}

    @Override
    public String nombre(){return this.nombre;}

    public void modificarRuta(String rutaReporteFallasHeladera) {
        String rutaFInal = "reportes/" + this.nombre;
        this.setPath(rutaFInal);
    }
}
