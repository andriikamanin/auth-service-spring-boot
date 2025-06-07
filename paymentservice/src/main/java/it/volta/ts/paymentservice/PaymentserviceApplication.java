package it.volta.ts.paymentservice;

import it.volta.ts.paymentservice.util.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentserviceApplication {

	public static void main(String[] args) {
		// Загружаем переменные из .env перед запуском Spring Boot
		DotenvLoader.loadEnv();

		SpringApplication.run(PaymentserviceApplication.class, args);
	}
}