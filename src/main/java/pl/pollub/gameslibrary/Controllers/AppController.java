package pl.pollub.gameslibrary.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Services.AppService;

@RestController
@RequestMapping(path="/apps")
public class AppController {
    @Autowired
    private AppService appService;

    @GetMapping(path = "")
    public Iterable<App> getAllUsers() {
        return appService.findAll();
    }

    @GetMapping(path = "/{id}")
    public App getUserById(@PathVariable("id") Long id) {
        return appService.findById(id);
    }

    @PostMapping(path = "")
    public App addUser(@RequestBody App app) {
        return appService.add(app);
    }

    @PutMapping(path = "/{id}")
    public App updateUser(@RequestBody App newApp, @PathVariable("id") Long id) {
        return appService.edit(newApp, id);
    }

    @DeleteMapping(path = "/{id}")
    public App deleteUser(@PathVariable("id") Long id) {
        return appService.del(id);
    }
}
