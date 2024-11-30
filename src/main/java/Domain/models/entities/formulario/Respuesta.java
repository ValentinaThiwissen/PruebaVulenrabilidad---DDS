package Domain.models.entities.formulario;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "respuesta")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pregunta_id")
    private Pregunta pregunta;

    @ManyToMany
    @JoinTable(name = "respuesta_opcion_respuesta", joinColumns = @JoinColumn(name = "respuesta_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "opcion_respuesta_id", referencedColumnName = "id"))
    private List <OpcionRespuesta>opcionesSeleccionadas;

    public Respuesta(Pregunta pregunta, List<OpcionRespuesta> opcionesSeleccionadas) {
        this.pregunta = pregunta;
        this.opcionesSeleccionadas = opcionesSeleccionadas;
    }
}
