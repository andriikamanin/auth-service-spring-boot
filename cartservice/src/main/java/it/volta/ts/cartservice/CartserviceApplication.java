package it.volta.ts.cartservice;

import it.volta.ts.cartservice.util.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CartserviceApplication {

	public static void main(String[] args) {
		DotenvLoader.loadEnv();
		SpringApplication.run(CartserviceApplication.class, args);
	}

}
