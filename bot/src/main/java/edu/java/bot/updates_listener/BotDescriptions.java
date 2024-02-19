package edu.java.bot.updates_listener;

public class BotDescriptions {
    private static final String NEW_LINE = "\n";

    private static final String DOUBLE_NEW_LINE = "\n\n";

    private static final String REPOSITORY_LINK = "https://github.com/not-Whale/tinkoff-java-backend-2024/";

    private static final String TELEGRAM_LINK = "@rezepinn";

    private BotDescriptions() {}

    public static String getBotDescription() {
        StringBuilder description = new StringBuilder();
        description
            .append("UpdatesNotificatorBot - это бот для удобного отслеживания изменений сервисов в одном месте. ")
            .append("На данный момент доступно отслеживание вопросов на StackOverflow и репозиториев Github.")
            .append(DOUBLE_NEW_LINE)
            .append("Подробнее о проекте:")
            .append(NEW_LINE)
            .append(REPOSITORY_LINK)
            .append(DOUBLE_NEW_LINE)
            .append("Cвяжитесь со мной ")
            .append(TELEGRAM_LINK)
            .append(", если Вы нашли ошибку или у Вас есть предложения по улучшению бота.");
        return description.toString();
    }

    public static String getBotShortDescription() {
        return "Бот для отслеживания изменений вопросов на StackOverflow и репозиториев Github.";
    }
}
