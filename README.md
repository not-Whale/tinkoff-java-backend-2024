![Bot](https://github.com/not-Whale/tinkoff-java-backend-2024/actions/workflows/bot.yml/badge.svg)
![Scrapper](https://github.com/not-Whale/tinkoff-java-backend-2024/actions/workflows/scrapper.yml/badge.svg)

# Backend-разработка на Java. Весна 2024
Решение заданий курса ["Backend-разработка на Java. Весна 2024"](https://fintech.tinkoff.ru/academy/java).

Студент: `Резепин Никита Игоревич`

Приложение для отслеживания обновлений контента по ссылкам.
При появлении новых событий отправляется уведомление в Telegram.

Проект написан на `Java 21` с использованием `Spring Boot 3`.

Проект состоит из 2-х приложений:
* Bot
* Scrapper

Для работы требуется БД `PostgreSQL`. Присутствует опциональная зависимость на `Kafka`.
