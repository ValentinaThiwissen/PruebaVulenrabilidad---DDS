package Domain.models.entities.notificacion;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Mail {
    private String destinatario;
    private String asunto;
    private String cuerpo;

    public Mail(String destinatario, String asunto, String cuerpo) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }
}