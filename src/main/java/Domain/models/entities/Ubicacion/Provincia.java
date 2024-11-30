package Domain.models.entities.Ubicacion;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="provincia")
public class Provincia {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "nombre", columnDefinition = "varchar(255)")
    private String nombre;

    public Provincia(){
        nombre = "Buenos Aires";
    }
}
