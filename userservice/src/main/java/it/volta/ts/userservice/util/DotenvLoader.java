package it.volta.ts.userservice.util;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvLoader {

    private static boolean loaded = false;

    public static void loadEnv() {
        if (loaded) return; // предотвратить повторную загрузку

        Dotenv dotenv = Dotenv.configure()
                .filename(".env")         // имя файла
                .ignoreIfMissing()        // не упадёт, если файла нет
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        loaded = true;
    }
}