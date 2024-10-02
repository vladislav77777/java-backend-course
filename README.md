![Bot](https://github.com/vladislav77777/java-backend-course/actions/workflows/bot.yml/badge.svg)
![Scrapper](https://github.com/vladislav77777/java-backend-course/actions/workflows/scrapper.yml/badge.svg)

# Link Tracker

FULL name: Grigorev Vladislav Vladimirovich

An application for tracking content updates via links.
When new events appear, a notification is sent to Telegram.

The project is written in `Java 21` using `Spring Boot 3'.

The project consists of 2 applications:
* Bot
* Scrapper

The `PostgreSQL` database is required for operation. There is an optional dependency on `Kafka'.
Prometheus and Grafana are used to collect and visualize metrics.


![image](https://github.com/vladislav77777/java-backend-course/assets/88504619/7aae0104-1f68-4a0f-bff2-034a53a81efe)
![image](https://github.com/vladislav77777/java-backend-course/assets/88504619/01c4bd8d-ec66-4e69-9b6a-306ffc08f99a)
