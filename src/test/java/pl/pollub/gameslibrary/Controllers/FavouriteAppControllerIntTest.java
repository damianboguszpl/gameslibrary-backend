package pl.pollub.gameslibrary.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.pollub.gameslibrary.GamesLibraryApplication;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Models.FavouriteApp;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Repositories.AppRepository;
import pl.pollub.gameslibrary.Repositories.FavouriteAppRepository;
import pl.pollub.gameslibrary.Repositories.UserRepository;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = GamesLibraryApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableAutoConfiguration()
@Slf4j
public class FavouriteAppControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FavouriteAppRepository favouriteAppRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppRepository appRepository;

    @Test
    @WithMockUser(authorities = { "ADMIN_ROLE", "USER_ROLE" })
    void canGetAllFavouriteApps() throws Exception {
        mvc.perform(get("/favapp")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // TODO: add asserts
    }

    @Test
    @WithMockUser(authorities = { "ADMIN_ROLE", "USER_ROLE" })
    void canGetFavouriteAppById() throws Exception {
        mvc.perform(get("/favapp/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // TODO: add asserts
    }

    @Test
    @WithMockUser(authorities = { "ADMIN_ROLE", "USER_ROLE" })
    void canCreateNewFavouriteApp() throws Exception {
        Optional<User> user = userRepository.findById(1L);
        Optional<App> app = appRepository.findById(2114607L);
        ObjectMapper objectMapper = new ObjectMapper();

        FavouriteApp favouriteApp = new FavouriteApp(null,app.get(),user.get());

        mvc.perform(post("/favapp")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(favouriteApp)))
                .andExpect(status().isOk())
                .andDo(print());

        // TODO: add asserts and check why user is null
    }

    @Test
    void canCreateNewFavouriteAppWhenAccessForbidden() throws Exception {
        Optional<User> user = userRepository.findById(1L);
        Optional<App> app = appRepository.findById(2114607L);
        ObjectMapper objectMapper = new ObjectMapper();

        FavouriteApp favouriteApp = new FavouriteApp(null,app.get(),user.get());
        mvc.perform(post("/favapp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favouriteApp)))
                .andExpect(status().isForbidden())
                .andDo(print());

        // TODO: add asserts and check why user is null
    }

    @Test
    @WithMockUser(authorities = { "ADMIN_ROLE", "USER_ROLE" })
    void canDeleteFavouriteApp() throws Exception {
        mvc.perform(delete("/favapp/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN_ROLE", "USER_ROLE" })
    void canEditFavouriteApp() throws Exception {
        Optional<User> user = userRepository.findById(2L);
        Optional<App> app = appRepository.findById(2114607L);
        ObjectMapper objectMapper = new ObjectMapper();

        FavouriteApp favouriteApp = new FavouriteApp(null,app.get(),user.get());

        mvc.perform(put("/favapp/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favouriteApp)))
                .andExpect(status().isOk())
                .andDo(print());

        // TODO: add asserts and check why user is null
    }
}