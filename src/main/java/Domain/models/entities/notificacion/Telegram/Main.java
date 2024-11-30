package Domain.models.entities.notificacion.Telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
   public static void main(String[] args) throws TelegramApiException {
      TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
      BotTelegram bot = new BotTelegram();
      botsApi.registerBot(bot);
      bot.enviarTexto(5227343355L, "Hello World!");
   }
}