### Hexlet tests and linter status:
[![Actions Status](https://github.com/CharNatalia/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/CharNatalia/java-project-72/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=CharNatalia_java-project-72&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=CharNatalia_java-project-72)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=CharNatalia_java-project-72&metric=bugs)](https://sonarcloud.io/summary/new_code?id=CharNatalia_java-project-72)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=CharNatalia_java-project-72&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=CharNatalia_java-project-72)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=CharNatalia_java-project-72&metric=coverage)](https://sonarcloud.io/summary/new_code?id=CharNatalia_java-project-72)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=CharNatalia_java-project-72&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=CharNatalia_java-project-72)



## Анализатор страниц (Java)
**[🌐 Анализатор страниц](https://java-project-72-xdbm.onrender.com)** — сайт, который анализирует указанные страницы на SEO-пригодность.


## Использование

- **Java** - основной язык разработки.
- **MVC** - архитектура.
- **Javalin** - веб-фреймворк для маршрутизации и обработки HTTP-запросов.
- **JTE** - шаблонизатор HTML-страниц.
- **Tailwind CSS** - оформление фронтенд-части..
- **JDBC** - взаимодействие с базой данных.
- **PostgreSQL и H2** - базы данных.
- **HikariCP** - пул соединений с базой данных.
- **Unirest** - выполнение HTTP-запросов к проверяемым сайтам.
- **Jsoup** - разбор HTML и извлечение данных страниц.
- **Lombok** - генерация конструкторов и геттеров.
- **Gradle** - сборка проекта и управление зависимостями.
- **JUnit, AssertJ и MockWebServer** - тестирование и имитация ответов сторонних сайтов.
- **Checkstyle** - проверка стиля кода.
- **JaCoCo** - измерение покрытия кода тестами.
- **SonarCloud** - статический анализ и контроль качества кода.
- **GitHub Actions** - автоматизация сборки и проверок.
- **Docker** - контейнеризация приложения.
- **Render** - размещение приложения.

## Использование

Для запуска понадобятся JDK 21, Node.js 22 с npm, Git и Make.

Склонируйте репозиторий и перейдите в папку приложения:

```bash
git clone https://github.com/CharNatalia/java-project-72.git
cd java-project-72/app
```

Установите зависимости, соберите CSS и приложение:

```bash
make setup
```

Запустите приложение в режиме разработки:

```bash
make start
```

Откройте [http://localhost:7070](http://localhost:7070). 

По умолчанию используется база H2 в памяти. Схема создаётся автоматически при запуске, данные удаляются после остановки приложения. 

Для подключения PostgreSQL задайте переменную `JDBC_DATABASE_URL`.

Порт приложения можно изменить через переменную `PORT`:

```bash
PORT=8080 make start
```

## Использование

1. Откройте главную страницу приложения.
2. Введите адрес сайта и добавьте его.
3. Нажмите кнопку запуска проверки.
4. Посмотрите результат: HTTP-статус, title, H1 и description.


