package edu.java.bot.updates_listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.request.SetMyDescription;
import com.pengrad.telegrambot.request.SetMyShortDescription;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.user_message_processors.UserMessageProcessor;
import java.util.List;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Bot implements AutoCloseable, UpdatesListener {
    private static final String BOT_DESCRIPTION = """
          UpdatesNotificatorBot - это бот для удобного отслеживания изменений сервисов в одном месте. \
          На данный момент доступно отслеживание вопросов на StackOverflow и репозиториев Github.

          Подробнее о проекте:
          https://github.com/not-Whale/tinkoff-java-backend-2024/

          Cвяжитесь со мной @rezepinn, если Вы нашли ошибку или у Вас есть предложения по улучшению бота.""";

    private static final String BOT_SHORT_DESCRIPTION =
        "Бот для отслеживания изменений вопросов на StackOverflow и репозиториев Github.";

    private final ApplicationConfig applicationConfig;

    private final UserMessageProcessor userMessageProcessor;

    private TelegramBot telegramBot;

    public Bot(@NonNull ApplicationConfig applicationConfig,
        @NonNull UserMessageProcessor userMessageProcessor) {
        this.applicationConfig = applicationConfig;
        this.userMessageProcessor = userMessageProcessor;
        setupTelegramBot();
    }

    private void setupTelegramBot() {
        this.telegramBot = new TelegramBot(applicationConfig.telegramToken());
        telegramBot.setUpdatesListener(
            this,
            e -> log.error(e.getStackTrace())
        );
        setupTelegramBotCommands();
        setupTelegramBotDescriptions();
    }

    private void setupTelegramBotCommands() {
        BotCommand[] botCommands = userMessageProcessor.commands().stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new);
        telegramBot.execute(new SetMyCommands(botCommands));
    }

    private void setupTelegramBotDescriptions() {
        telegramBot.execute(new SetMyDescription()
            .description(BOT_DESCRIPTION)
            .languageCode(applicationConfig.languageCode())
        );
        telegramBot.execute(new SetMyShortDescription()
            .description(BOT_SHORT_DESCRIPTION)
            .languageCode(applicationConfig.languageCode())
        );
    }

    @Override
    public int process(@NonNull List<Update> updates) {
        for (Update update : updates) {
            if (update.message() != null) {
                telegramBot.execute(
                    userMessageProcessor.process(update)
                );
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(@NonNull BaseRequest<T, R> request) {
        return telegramBot.execute(request);
    }

    @Override
    public void close() {
        telegramBot.shutdown();
    }
}
