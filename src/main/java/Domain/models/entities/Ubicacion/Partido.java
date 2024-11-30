package Domain.models.entities.Ubicacion;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="partido")
public class Partido {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "nombre", columnDefinition = "varchar(255)")
    private String nombre;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;


    public Partido(){
        nombre = "Laprida";
        provincia = new Provincia();
    }
}
