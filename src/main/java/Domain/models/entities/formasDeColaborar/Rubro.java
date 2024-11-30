package Domain.models.entities.formasDeColaborar;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@Entity
@Table(name = "Rubro")
public class Rubro {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "nombreRubro", columnDefinition = "varchar(255)")
    private String nombreRubro;

    public Rubro(String nombre) {
        nombreRubro= nombre;
    }

    public Rubro() {

    }
}
