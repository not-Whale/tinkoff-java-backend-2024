package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.Database;
import edu.java.bot.db.user.State;
import lombok.Setter;

@Setter
public class ListCommand implements Command {
    private static final String COMMAND = "/list";

    private static final String DESCRIPTION = "Вывести список отслеживаемых ресурсов.";

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
        if (!database.hasUser(userId) || !database.getUserState(userId).equals(State.DEFAULT)) {
            return new SendMessage(
                userId,
                "Для использования команды "
                    + COMMAND
                    + " вам необходимо запустить бота и зарегистрироваться с помощью /start"
            );
        } else {
            String[] sources = database.getUserLinks(userId);
            if (sources.length == 0) {
                return new SendMessage(
                    userId,
                    "Список отслеживаемых ресурсов пуст!"
                );
            } else {
                StringBuilder stringBuilder = new StringBuilder("Список отслеживаемых ресурсов:\n");
                for (String source : sources) {
                    stringBuilder
                        .append("- ")
                        .append(source)
                        .append("\n");
                }
                return new SendMessage(userId, stringBuilder.toString());
            }
        }
    }
}
