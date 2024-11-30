package Domain.models.entities.reporte.exportable;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
/*
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name ="ReporteExportable")
@DiscriminatorColumn(name = "Exportable")**/
public abstract class  Exportable {
    public void setNombre(String nombre) {

    }

    public String nombre() {
        return null;
    }

    public Map<String, List<String>> getDatos() {

        return null;
    }


}
