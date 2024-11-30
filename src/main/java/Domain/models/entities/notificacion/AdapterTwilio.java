package Domain.models.entities.notificacion;

import com.twilio.rest.api.v2010.account.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface  AdapterTwilio {

    public void enviarWhatsapp(Notificacion notificacion);
}
