package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.Database;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
public class Bot implements AutoCloseable, UpdatesListener {
    @Autowired
    private final ApplicationConfig applicationConfig;

    private final Database database;

    private final TelegramBot telegramBot;

    private final Command[] commands;

    public Bot(ApplicationConfig applicationConfig, Database database, Command[] commands) {
        this.applicationConfig = applicationConfig;
        this.database = database;
        this.telegramBot = new TelegramBot(applicationConfig.telegramToken());
        this.commands = commands;
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() == null) {
                continue;
            }
            Optional<Command> command = Arrays.stream(commands)
                .filter(cmd -> cmd.validate(update))
                .findFirst();
            if (command.isEmpty()) {
                telegramBot.execute(
                    new SendMessage(update.message().from().id(),
                        "Команда неизвестна!")
                );
            } else {
                telegramBot.execute(command.get().handle(update));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void run() {
        for (Command command : commands) {
            command.setDatabase(database);
        }
        BotCommand[] botCommands = Arrays.stream(commands)
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new);
        telegramBot.execute(new SetMyCommands(botCommands));
        telegramBot.setUpdatesListener(this, e -> log.warn(e.getStackTrace()));
    }

    @Override
    public void close() throws Exception {
        telegramBot.shutdown();
    }
}
