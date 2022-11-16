package pl.pollub.gameslibrary;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pollub.gameslibrary.Models.Role;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Services.AppService;
import pl.pollub.gameslibrary.Services.RoleService;
import pl.pollub.gameslibrary.Services.SteamApiService;
import pl.pollub.gameslibrary.Services.UserService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

//@Slf4j
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

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService, RoleService roleService) {
		return  args -> {
			roleService.add(new Role(null, "USER_ROLE"));
			roleService.add(new Role(null, "ADMIN_ROLE"));

			userService.add(new User(null, "user", "user@email.com", "password", new ArrayList<>(), new ArrayList<>()));
			userService.add(new User(null, "admin", "admin@email.com", "password", new ArrayList<>(), new ArrayList<>()));

			roleService.addRoleToUser("user@email.com", "USER_ROLE");
			roleService.addRoleToUser("admin@email.com", "USER_ROLE");
			roleService.addRoleToUser("admin@email.com", "ADMIN_ROLE");

		};
	}

//	@PostConstruct
////	public void loadAppsFromApi() {
//	public void loadAppsFromApi() throws IOException, InterruptedException, JSONException {
//
//		SteamApiService.pullNewApps(appService);
////		steamApiService.rebase(appService);
//
//	}
}
