package it.volta.ts.cartservice.util;


import io.github.cdimascio.dotenv.Dotenv;

public class DotenvLoader {

    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env") // имя файла (по умолчанию .env)
                .ignoreIfMissing() // чтобы не упал, если .env нет
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
    }
}