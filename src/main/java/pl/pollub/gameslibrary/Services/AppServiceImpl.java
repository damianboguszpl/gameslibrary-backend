package pl.pollub.gameslibrary.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Repositories.AppRepository;

import java.util.Optional;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private AppRepository appRepository;

    @Autowired
    public ResponseEntity<DetailedResponse> add(App app) {
        if(app.getId() != null) {
            Optional<App> appOptional = appRepository.findById(app.getId());
            if(appOptional.isPresent())
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new DetailedResponse("APP_ALREADY_EXISTS", "An App with specified Id already exists.", null));
        }
        if(app.getTitle() != null && app.getDescription() != null && app.getShortDescription() != null
                && app.getDevelopers() != null && app.getPublishers() != null && app.getScreenshotLink() != null) {
            appRepository.save(app);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new DetailedResponse("NEW_APP_CREATED", "New App has been created.", null));
        }
        else return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Required parameters not specified.", null));
    }

    public ResponseEntity<DetailedResponse> edit(App newApp, Long id) {
        if(id == null) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Id parameter not specified.", null));
        if(newApp.getTitle() == null && newApp.getDescription()  == null && newApp.getShortDescription()  == null
                && newApp.getScreenshotLink()  == null && newApp.getDevelopers()  == null && newApp.getPublishers()  == null )
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "No parameters to update.", null));

        App app = appRepository.findById(id).orElse(null);

        if(app != null) {
            app.setTitle(newApp.getTitle()!=null?newApp.getTitle():app.getTitle());
            app.setDescription(newApp.getDescription()!=null?newApp.getDescription():app.getDescription());
            app.setShortDescription(newApp.getShortDescription()!=null?newApp.getShortDescription():app.getShortDescription());
            app.setScreenshotLink(newApp.getScreenshotLink()!=null?newApp.getScreenshotLink():app.getScreenshotLink());
            app.setDevelopers(newApp.getDevelopers()!=null?newApp.getDevelopers():app.getDevelopers());
            app.setPublishers(newApp.getPublishers()!=null?newApp.getPublishers():app.getPublishers());

            appRepository.save(app);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DetailedResponse("APP_UPDATED", "App has been updated.", app));
        }
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new DetailedResponse("APP_NOT_FOUND", "App does not exist.", null));
    }

    public ResponseEntity<DetailedResponse> delete(Long id) {
        if(id == null) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Id parameter not specified.", null));

        App app = appRepository.findById(id).orElse(null);

        if (app != null) {
            appRepository.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DetailedResponse("APP_DELETED", "App has been deleted.", null));
        }
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new DetailedResponse("APP_NOT_FOUND", "App does not exist.", null));
    }

    public Iterable<App> getAll() {
        return appRepository.findAll();
    }

    public App getById(Long id) {
        return appRepository.findById(id).orElse(null);
    }

    public Iterable<App> getByType(String type) {
        return appRepository.findByType(type);
    }
}
