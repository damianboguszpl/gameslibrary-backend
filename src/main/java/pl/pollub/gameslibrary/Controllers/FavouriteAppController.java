package pl.pollub.gameslibrary.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.FavouriteApp;
import pl.pollub.gameslibrary.Services.FavouriteAppService;

@RestController
@RequestMapping(path="/favourite")
public class FavouriteAppController {
    @Autowired
    private FavouriteAppService favouriteAppService;

    @GetMapping(path = "")
    public Iterable<FavouriteApp> getAllFavouriteApps() {
        return favouriteAppService.findAll();
    }

    @GetMapping (path = "/{id}")
    public FavouriteApp getFavouriteAppById(@PathVariable("id") Long id) {
        return favouriteAppService.findById(id);
    }

    @PostMapping(path = "")
    public FavouriteApp addFavouriteApp(@RequestBody FavouriteApp favouriteApp) {
        return favouriteAppService.add(favouriteApp);
    }

    @PutMapping(path = "/{id}")
    public FavouriteApp updateFavouriteApp(@RequestBody FavouriteApp favouriteApp, @PathVariable("id") Long id) {
        return favouriteAppService.edit(favouriteApp, id);
    }

    @DeleteMapping(path = "/{id}")
    public FavouriteApp deleteFavouriteApp(@PathVariable("id") Long id) {
        return favouriteAppService.del(id);
    }
}
