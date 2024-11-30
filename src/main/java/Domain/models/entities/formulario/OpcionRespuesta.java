package Domain.models.entities.formulario;

import javax.persistence.*;

@Entity
@Table(name="opcion_respuesta")
public class OpcionRespuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "posibleRespuesta")
    private String posibleRespuesta;

    public OpcionRespuesta(String posibleRespuesta) {
        this.posibleRespuesta = posibleRespuesta;
    }
    public OpcionRespuesta() {
    }
}
