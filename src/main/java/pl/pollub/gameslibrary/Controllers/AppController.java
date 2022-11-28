package pl.pollub.gameslibrary.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Services.AppService;

import java.util.Objects;

@RestController
@RequestMapping(path="/app")
public class AppController {
    @Autowired
    private AppService appService;

    @PostMapping(path = "")
    public ResponseEntity<DetailedResponse> addApp(@RequestBody App app) {
        ResponseEntity<DetailedResponse> newApp = appService.add(app);
        return Objects.requireNonNullElseGet(newApp, () -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> updateApp(@RequestBody App newApp, @PathVariable("id") Long id) {
        return appService.edit(newApp, id);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> deleteApp(@PathVariable("id") Long id) {
        return appService.delete(id);
    }

    @GetMapping(path = "")
    public Iterable<App> getAllApps() {
        return appService.getAll();
    }

    @GetMapping(path = "/{id}")
    public App getAppById(@PathVariable("id") Long id) {
        return appService.getById(id);
    }

    @GetMapping(path = "/type/{type}")
    public Iterable<App> getAppByType(@PathVariable("type") String type) {
        return appService.getByType(type);
    }
}
