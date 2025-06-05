package it.volta.ts.authservice;

import it.volta.ts.authservice.util.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthserviceApplication {

	public static void main(String[] args) {
		DotenvLoader.loadEnv();
		SpringApplication.run(AuthserviceApplication.class, args);
	}

}
