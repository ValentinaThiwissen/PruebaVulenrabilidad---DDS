package Domain.models.entities.heladera;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Table(name ="Modelo")
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="nombreModelo", columnDefinition ="TEXT")
    private String nombreModelo;

    @Column(name = "temperaturaMinima")
    private Integer temperaturaMinima;

    @Column(name = "temperaturaMaxima")
    private Integer temperaturaMaxima;

    public Modelo (String nombre, Integer min, Integer max){
        nombreModelo = nombre;
        temperaturaMinima = min;
        temperaturaMaxima = max;
    }
    public Modelo (){
    }

}

