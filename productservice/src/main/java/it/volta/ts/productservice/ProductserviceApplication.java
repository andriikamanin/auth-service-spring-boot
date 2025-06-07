package it.volta.ts.productservice;

import it.volta.ts.productservice.util.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductserviceApplication {

	public static void main(String[] args) {
		DotenvLoader.loadEnv();
		SpringApplication.run(ProductserviceApplication.class, args);
	}

}
