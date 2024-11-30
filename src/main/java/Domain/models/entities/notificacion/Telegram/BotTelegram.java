package Domain.models.entities.notificacion.Telegram;


import Domain.models.entities.notificacion.AdapterJavaxEmail;
import Domain.models.entities.notificacion.AdapterTelegram;
import Domain.models.entities.notificacion.Notificacion;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class BotTelegram extends TelegramLongPollingBot implements AdapterTelegram {

    @Override
    // devuelve el nombre del usuario del bot
    public String getBotUsername() {
        return "disenioBot";
    }
    @Override
    //devuelve el token del bot
    public String getBotToken() {
        return "7321239835:AAHrADvP9c_6PZ20XRRnubxY6EtIhzx2Uqw";
    }    //we handle the received update and capture the text and id of the conversation
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            // Aquí puedes procesar el mensaje recibido y decidir qué responder
            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());
            message.setText("Hola! Has enviado: " + messageText);

            try {
                execute(message); // Enviar el mensaje al chat
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    private SendMessage generarMensaje(Long chatId, int cantidadCaracteres) {
        return new SendMessage(chatId.toString(), "El mensaje tiene " + cantidadCaracteres + " caracteres");
    }

    public void enviarMensaje(Notificacion notificacion) {
        SendMessage mensaje = new SendMessage();
        mensaje.setChatId(notificacion.getDestinatario().getNombre());
        mensaje.setText(notificacion.getMensaje());
        try {
            execute(mensaje);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void registerBot(BotTelegram botTelegram) {

    }
    public void enviarTexto(Long chatId, String texto) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId.toString())
                .text(texto)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}


