package it.volta.ts.paymentservice.util;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvLoader {

    private static boolean loaded = false;

    public static void loadEnv() {
        if (loaded) return;

        Dotenv dotenv = Dotenv.configure()
                .filename(".env")
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        loaded = true;
    }
}