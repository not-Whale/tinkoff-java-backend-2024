package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.Database;
import edu.java.user.State;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Setter;

@Setter
public class UntrackCommand implements Command {
    private static final String COMMAND = "/untrack";

    private static final String USE_FORMAT = "/untrack <source>";

    private static final String DESCRIPTION = "Удалить ресурс из списка отслеживаемых: " + USE_FORMAT;

    // TODO: регулярку на ресурс
    private static final String SOURCE_FORMAT = "https?://(github.com|stackoverflow.com)(/[\\w.#-]*)*";

    private static final String COMMAND_FORMAT = COMMAND + " (" + SOURCE_FORMAT + ")";

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
            String source = getSource(update);
            if (source == null) {
                return new SendMessage(
                    userId,
                    "Неверный формат ссылки!"
                );
            }
            if (database.untrackLinkForUser(userId, source)) {
                return new SendMessage(
                    userId,
                    "Ресурс удален из списка отслеживаемых!"
                );
            } else {
                return new SendMessage(
                    userId,
                    "Ресурса нет в списке отслеживаемых!"
                );
            }
        }
    }

    private String getSource(Update update) {
        Matcher matcher = Pattern.compile(COMMAND_FORMAT).matcher(update.message().text());
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
