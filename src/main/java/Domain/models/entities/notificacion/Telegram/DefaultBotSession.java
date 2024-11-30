package Domain.models.entities.notificacion.Telegram;

import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

public class DefaultBotSession implements BotSession {
    @Override
    public void setOptions(BotOptions botOptions) {

    }

    @Override
    public void setToken(String s) {

    }

    @Override
    public void setCallback(LongPollingBot longPollingBot) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
