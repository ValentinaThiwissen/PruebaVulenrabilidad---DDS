package Domain.models.entities.formulario;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
@Data
@Entity
@Table(name = "pregunta")
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consulta", columnDefinition = "varchar(255)")
    private  String consulta;
    @Column
    private Boolean esObligatorio;

    @Enumerated(EnumType.STRING)
    private TipoPregunta tipoPregunta;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "pregunta_opcion_respuesta", joinColumns = @JoinColumn(name = "pregunta_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "opcion_respuesta_id", referencedColumnName = "id"))
    private List <OpcionRespuesta>opciones;

    public Pregunta(String consulta, Boolean esObligatoria, TipoPregunta tipoPregunta, List<OpcionRespuesta> opciones) {
        this.consulta = consulta;
        this.esObligatorio = esObligatoria;
        this.tipoPregunta = tipoPregunta;
        this.opciones = opciones;
    }
    public Pregunta(String consulta, Boolean esObligatoria, TipoPregunta tipoPregunta) {
        this.consulta = consulta;
        this.esObligatorio = esObligatoria;
        this.tipoPregunta = tipoPregunta;
    }
    public Pregunta() {
    }
}
