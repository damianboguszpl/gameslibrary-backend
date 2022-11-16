package pl.pollub.gameslibrary.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Services.AppService;

@RestController
@RequestMapping(path="/app")
public class AppController {
    @Autowired
    private AppService appService;

    @GetMapping(path = "")
    public Iterable<App> getAllApps() {
        return appService.findAll();
    }

    @GetMapping(path = "/{id}")
    public App getAppById(@PathVariable("id") Long id) {
        return appService.findById(id);
    }

    @PostMapping(path = "")
    public App addApp(@RequestBody App app) {
        return appService.add(app);
    }

    @PutMapping(path = "/{id}")
    public App updateApp(@RequestBody App newApp, @PathVariable("id") Long id) {
        return appService.edit(newApp, id);
    }

    @DeleteMapping(path = "/{id}")
    public App deleteApp(@PathVariable("id") Long id) {
        return appService.del(id);
    }
}
