package Domain.models.entities.notificacion;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import javax.mail.MessageAware;
import java.net.URI;
import java.math.BigDecimal;

public class NotifiarWhatsApp implements AdapterTwilio{
    public static final String ACCOUNT_SID = "AC7774036bfa4d06005577ec5a549c9822";
    public static final String AUTH_TOKEN = "[5bd906d0efd0aa9c18ea7eee060a6584]";

    public void enviarWhatsapp(Notificacion notificacion){
    }
    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:+5491121764392"),
                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                "Ha llegado tu mensaje").create();

        System.out.println(message.getSid());
    }
}