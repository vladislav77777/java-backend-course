![Bot](https://github.com/vladislav77777/java-backend-course/actions/workflows/bot.yml/badge.svg)
![Scrapper](https://github.com/vladislav77777/java-backend-course/actions/workflows/scrapper.yml/badge.svg)

# Link Tracker

ФИО: Григорьев Владислав Владимирович

Приложение для отслеживания обновлений контента по ссылкам.
При появлении новых событий отправляется уведомление в Telegram.

Проект написан на `Java 21` с использованием `Spring Boot 3`.

Проект состоит из 2-х приложений:
* Bot
* Scrapper

Для работы требуется БД `PostgreSQL`. Присутствует опциональная зависимость на `Kafka`.
Используется `Prometheus`,`Grafana` для сбора и визуализации метрик.


![image](https://github.com/vladislav77777/java-backend-course/assets/88504619/7aae0104-1f68-4a0f-bff2-034a53a81efe)
![image](https://github.com/vladislav77777/java-backend-course/assets/88504619/01c4bd8d-ec66-4e69-9b6a-306ffc08f99a)
