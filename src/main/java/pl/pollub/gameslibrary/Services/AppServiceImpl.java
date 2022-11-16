package pl.pollub.gameslibrary.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Repositories.AppRepository;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private AppRepository appRepository;

    public Iterable<App> findAll() {
        return appRepository.findAll();
    }

    public App findById(Long id) {
        return appRepository.findById(id).orElse(null);
    }

    public App edit(App newApp, Long id) {
        App app = appRepository.findById(id).orElse(null);

        if(app != null) {
            app.setTitle(newApp.getTitle()!=null?newApp.getTitle():app.getTitle());
            app.setDescription(newApp.getDescription()!=null?newApp.getDescription():app.getDescription());
            app.setShortDescription(newApp.getShortDescription()!=null?newApp.getShortDescription():app.getShortDescription());
            app.setScreenshotLink(newApp.getScreenshotLink()!=null?newApp.getScreenshotLink():app.getScreenshotLink());
            app.setDevelopers(newApp.getDevelopers()!=null?newApp.getDevelopers():app.getDevelopers());
            app.setPublishers(newApp.getPublishers()!=null?newApp.getPublishers():app.getPublishers());

            return appRepository.save(app);
        }
        else return null;
    }

    @Autowired
    public App add(App app) {
//        if (app != null) {
//            return appRepository.save(app);
//        }
//        else return null;
        if(app.getTitle() != null && app.getDescription() != null && app.getShortDescription() != null
        && app.getDevelopers() != null && app.getPublishers() != null && app.getScreenshotLink() != null) {
            return appRepository.save(app);
        }
        else return null;
    }

    public App del(Long id) {
        App app = appRepository.findById(id).orElse(null);

        if (app != null) {
            appRepository.deleteById(id);
            return app;
        }
        else return null;
    }
}
