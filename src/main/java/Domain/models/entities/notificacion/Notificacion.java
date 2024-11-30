package Domain.models.entities.notificacion;

import Domain.models.entities.colaborador.Colaborador;
import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.persistence.*;

@Data
@Entity
@Table(name ="Notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name ="destinatario_id")
    private Colaborador destinatario = null;

    @Column(name = "mensaje", columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "estado")
    private Boolean estado;

    @Column
    private String asunto;


    public Notificacion(Colaborador destinatario, String mensaje, String asunto) {
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.asunto = asunto;

    }

    public Notificacion() {

    }

    public Notificacion(Colaborador o, String mensaje) {
    }
}
