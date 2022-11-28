package pl.pollub.gameslibrary.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Models.FavouriteApp;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Repositories.AppRepository;
import pl.pollub.gameslibrary.Repositories.FavouriteAppRepository;
import pl.pollub.gameslibrary.Repositories.UserRepository;

import java.util.Optional;

@Slf4j
@Service
public class FavouriteAppServiceImpl implements FavouriteAppService {
    @Autowired
    private FavouriteAppRepository favouriteAppRepository;
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<DetailedResponse> add(Long appId, Long userId) {
            if (appId == null)
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "AppId parameter not specified.", null));
            if (userId == null)
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "UserId parameter not specified.", null));

            Optional<App> appOptional = appRepository.findById(appId);
            Optional<User> userOptional = userRepository.findById(userId);

            if(appOptional.isPresent()) {
                if(userOptional.isPresent()) {
                    App app = appOptional.get();
                    User user = userOptional.get();
                    FavouriteApp existingFavouriteApp = favouriteAppRepository.findByUserAndApp(user, app);
                    if(existingFavouriteApp == null) {
                        FavouriteApp favouriteApp = new FavouriteApp(null, app, user);
                        favouriteAppRepository.save(favouriteApp);
                        return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(new DetailedResponse("NEW_FAVOURITE_APP_CREATED", "New FavouriteApp has been created.", null));
                    }
                    else return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(new DetailedResponse("APP_ALREADY_FAVOURITE", "App is already favourite.", null));
                }
                else return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Specified User does not exist.", null));
            } else return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Specified App does not exist.", null));
    }

    public ResponseEntity<DetailedResponse> edit(Long appId, Long userId, Long id) {

        if(appId == null && userId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Neither UserId nor AppId parameter specified.", null));
        }
        if(id == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Id parameter not specified.", null));
        }

        FavouriteApp favouriteApp = favouriteAppRepository.findById(id).orElse(null);
        if (favouriteApp != null) {
            App app = null;
            User user = null;

            if(appId != null) {
                Optional<App> appOptional = appRepository.findById(appId);
                if(appOptional.isPresent()) {
                    app = appOptional.get();
                    favouriteApp.setApp(app);
                }
            }
            if(userId != null) {
                Optional<User> userOptional = userRepository.findById(userId);
                if(userOptional.isPresent()) {
                    user = userOptional.get();
                    favouriteApp.setUser(user);
                }
            }
            if( app == null && user == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Both specified App and User do not exist.", null));
            }
            else {
                favouriteAppRepository.save(favouriteApp);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new DetailedResponse("FAVOURITE_APP_UPDATED", "FavouriteApp has been updated.", favouriteApp));
            }
        }
        else return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new DetailedResponse("FAVOURITE_APP_NOT_FOUND", "FavouriteApp does not exist.", null));
    }

    public ResponseEntity<DetailedResponse> delete(Long id) {
        FavouriteApp favouriteApp = favouriteAppRepository.findById(id).orElse(null);

        if (favouriteApp != null) {
            favouriteAppRepository.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DetailedResponse("FAVOURITE_APP_DELETED", "FavouriteApp has been deleted.", null));
        }
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new DetailedResponse("FAVOURITE_APP_NOT_FOUND", "FavouriteApp does not exist.", null));
    }

    public Iterable<FavouriteApp> getAll() {
        return favouriteAppRepository.findAll();
    }

    public FavouriteApp getById(Long id) {
        return favouriteAppRepository.findById(id).orElse(null);
    }

    public Iterable<FavouriteApp> getByUserId(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return favouriteAppRepository.findByUserIs(user);
        }
        else return null;
    }

    public FavouriteApp getByUserEmailAndAppId(String userEmail, Long appId) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userEmail));
        Optional<App> appOptional = appRepository.findById(appId);
        if(userOptional.isPresent() && appOptional.isPresent()) {
            User user = userOptional.get();
            App app = appOptional.get();
            return favouriteAppRepository.findByUserAndApp(user, app);
        }
        else return null;
    }
}
