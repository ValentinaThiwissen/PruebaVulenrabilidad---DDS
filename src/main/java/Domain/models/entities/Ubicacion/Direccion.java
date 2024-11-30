package Domain.models.entities.Ubicacion;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="direccion")
public class Direccion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "calle", columnDefinition = "varchar(255)")
    private String  calle;

    @Column(name = "altura", columnDefinition = "varchar(255)")
    private Integer altura;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "localidad_id" )
    private Localidad localidad;

    public Direccion(String nombre, Integer alt){
        calle = nombre;
        altura = alt;
        localidad = new Localidad();
    }

    public Direccion() {

    }
}
