FROM node:22 AS styles

WORKDIR /app

COPY /app .

RUN npm ci

RUN npm run build:css

FROM gradle:8.12.1-jdk21

WORKDIR /app

COPY /app .

COPY --from=styles /app/src/main/resources/static /app/src/main/resources/static

RUN ["./gradlew", "clean", "build"]

CMD ["./gradlew", "run"]