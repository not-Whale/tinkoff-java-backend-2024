package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.Database;
import edu.java.bot.db.user.State;
import lombok.Setter;

@Setter
public class StartCommand implements Command {
    private static final String COMMAND = "/start";

    private static final String DESCRIPTION = "Запустить бота и зарегистрироваться.";

    private Database database;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        if (!validate(update)) {
            throw new IllegalArgumentException(
                "Неверный формат команды! "
                    + "Ожидается: "
                    + COMMAND
                    + ", получено: "
                    + update.message().text()
            );
        }
        if (database == null) {
            throw new RuntimeException("База данных не подключена!");
        }
        long userId = update.message().from().id();
        if (!database.hasUser(userId)) {
            database.createUser(userId);
        }
        if (database.getUserState(userId).equals(State.DEFAULT)) {
            return new SendMessage(userId, "Вы уже зарегистрированы!");
        } else {
            database.setUserState(userId, State.DEFAULT);
            return new SendMessage(
                userId,
                "Вы успешно зарегистрированы, " + update.message().from().firstName() + "!"
            );
        }
    }
}
