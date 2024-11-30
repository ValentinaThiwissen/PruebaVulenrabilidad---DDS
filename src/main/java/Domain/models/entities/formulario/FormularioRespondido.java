package Domain.models.entities.formulario;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
@Table(name = "formulario_respondido")
public class FormularioRespondido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "formulario_id")
    private Formulario unFormulario;

    @ManyToMany
    @JoinTable(name = "formulario_respondido_respuesta", joinColumns = @JoinColumn(name = "formulario_respondido_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "respuesta_id", referencedColumnName = "id"))
    private List<Respuesta> respuestas;

    @Column(name = "fechaDeRegistro",  columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaDeRegistro;

    public FormularioRespondido(Formulario unFormulario, List<Respuesta> respuestas, LocalDateTime fechaDeRegistro) {
        this.unFormulario = unFormulario;
        this.respuestas = respuestas;
        this.fechaDeRegistro = fechaDeRegistro;
    }
}
