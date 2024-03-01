package edu.java.bot.user_message_processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import lombok.NonNull;
import java.util.List;

public interface UserMessageProcessor {
    List<Command> commands();

    SendMessage process(@NonNull Update update);
}
