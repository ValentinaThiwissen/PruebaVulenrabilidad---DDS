package Domain.models.entities.notificacion;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface AdapterTelegram {

    public void enviarMensaje (Notificacion notificacion);
}
