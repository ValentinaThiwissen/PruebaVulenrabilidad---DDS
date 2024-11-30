package Domain.models.entities.vianda;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
@Data
@Embeddable
public class Comida {

    private String nombre;
    private Integer calorias;
    private LocalDate fechaDeCaducidad;

    public Comida(){
        nombre = "Pizza";
        calorias = 100;
        fechaDeCaducidad = LocalDate.now();
    }

    public Comida(String nombre2){
        nombre = nombre2;
        calorias = 100;
        fechaDeCaducidad = LocalDate.now();
    }
}
