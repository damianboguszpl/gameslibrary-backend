package pl.pollub.gameslibrary;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.pollub.gameslibrary.Services.AppService;
import pl.pollub.gameslibrary.Services.SteamApiService;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class GamesLibraryApplication {
	@Autowired
	AppService appService;

	@Autowired
	SteamApiService steamApiService;

	public static void main(String[] args) {
		SpringApplication.run(GamesLibraryApplication.class, args);

	}

	@PostConstruct
	public void loadAppsFromApi() {
//	public void loadAppsFromApi() throws IOException, InterruptedException, JSONException {

//		SteamApiService.pullNewApps(appService);
//		steamApiService.rebase(appService);

	}
}
