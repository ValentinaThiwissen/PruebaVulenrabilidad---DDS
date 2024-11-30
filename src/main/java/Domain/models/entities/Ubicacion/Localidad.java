package Domain.models.entities.Ubicacion;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="localidad")
public class Localidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "varchar(255)")
    private String nombre;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name ="partido_id")
    private Partido partido;

    public Localidad(){
        nombre = "Laprida";
        partido = new Partido();
    }


}
