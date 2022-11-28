package pl.pollub.gameslibrary;

import org.json.JSONException;
import org.modelmapper.ModelMapper;
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
import pl.pollub.gameslibrary.Services.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;

//@Slf4j
@SpringBootApplication
@EnableScheduling
public class GamesLibraryApplication {
	@Autowired
	AppService appService;

//	@Autowired
//	SteamApiService steamApiService;

	public static void main(String[] args) {
		SpringApplication.run(GamesLibraryApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
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

			userService.add(new User(null, "arturnowak4@gmail.com", "arturnowak4@gmail.com", "Ab3c@dlo12", new ArrayList<>(), new ArrayList<>()));
			roleService.addRoleToUser("arturnowak4@gmail.com", "USER_ROLE");
			roleService.addRoleToUser("arturnowak4@gmail.com", "ADMIN_ROLE");

		};
	}

//	@PostConstruct
////	public void loadAppsFromApi() {
//	public void loadAppsFromApi() throws IOException, InterruptedException, JSONException {
//
////		SteamApiService.pullNewApps(appService);
//		SteamApiService.rebase(appService);
//
//	}
}