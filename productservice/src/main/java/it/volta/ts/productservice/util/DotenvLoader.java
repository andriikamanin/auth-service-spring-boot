package it.volta.ts.productservice.util;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvLoader {

    private static boolean loaded = false;

    public static void loadEnv() {
        if (loaded) return;

        Dotenv dotenv = Dotenv.configure()
                .filename(".env")         // имя файла по умолчанию
                .ignoreIfMissing()        // не упадёт, если нет файла
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        loaded = true;
    }
}