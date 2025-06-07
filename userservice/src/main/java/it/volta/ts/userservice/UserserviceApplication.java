package it.volta.ts.userservice;

import it.volta.ts.userservice.util.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		DotenvLoader.loadEnv();
		SpringApplication.run(UserserviceApplication.class, args);
	}

}
