package Domain.models.entities.notificacion;

import Domain.models.entities.colaborador.MedioDeContacto;
import Domain.models.entities.colaborador.TipoDeContacto;
import Domain.models.entities.notificacion.Telegram.BotTelegram;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Notificador {
//no persistir whatsapp, email,telegram
private static Notificador instancia;
    private static AdapterTwilio whatsappAdapter;
    private static AdapterJavaxEmail emailAdapter;
    private static AdapterTelegram telegramAdapter;

    public static Notificador obtenerInstancia() { // Singleton
        if (instancia == null){
            instancia = new Notificador();
            whatsappAdapter = new NotifiarWhatsApp();
            telegramAdapter = new BotTelegram();
            emailAdapter= new NotificarEmail();
        }
        return instancia;
    }
    public void enviarNotificacion(Notificacion notificacion, TipoDeContacto medio) {

        switch (medio){
            case WHATSAPP :
                whatsappAdapter.enviarWhatsapp(notificacion);
                break;

            case MAIL:
                emailAdapter.enviarMail(notificacion);
                break;

            case TELEGRAM :
                telegramAdapter.enviarMensaje(notificacion);

            default:

                break;
        }

    }
}
