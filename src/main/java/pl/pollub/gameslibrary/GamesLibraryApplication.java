package pl.pollub.gameslibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GamesLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamesLibraryApplication.class, args);
	}
}
