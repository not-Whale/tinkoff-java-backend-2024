package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.Database;
import edu.java.user.State;
import java.util.List;
import lombok.Setter;

@Setter
public class HelpCommand implements Command {
    private static final String COMMAND = "/help";

    private static final String DESCRIPTION = "Показать список команд.";

    private Database database;

    private List<Command> commands;

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
            if (commands == null) {
                throw new RuntimeException("Список команд не указан!");
            }
            StringBuilder stringBuilder = new StringBuilder("Список команд:\n");
            for (Command command : commands) {
                stringBuilder
                    .append(command.command())
                    .append(" - ")
                    .append(command.description())
                    .append("\n");
            }
            return new SendMessage(userId, stringBuilder.toString());
        }
    }
}
