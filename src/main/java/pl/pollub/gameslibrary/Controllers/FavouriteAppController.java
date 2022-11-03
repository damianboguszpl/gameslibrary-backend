package pl.pollub.gameslibrary.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.FavouriteApp;
import pl.pollub.gameslibrary.Repositories.FavouriteAppRepository;

@RestController
@RequestMapping(path="/favourite")
public class FavouriteAppController {
    @Autowired
    private FavouriteAppRepository favouriteAppRepository;

    @GetMapping(path = "")
    public Iterable<FavouriteApp> getAllFavouriteApps() {
        return favouriteAppRepository.findAll();
    }

    @GetMapping (path = "/{id}")
    public FavouriteApp getFavouriteAppById(@PathVariable("id") Long id) {
        return favouriteAppRepository.findById(id).orElse(null);
    }

    @PostMapping(path = "")
    public FavouriteApp addFavouriteApp(@RequestBody FavouriteApp favouriteApp) {
        return favouriteAppRepository.save(favouriteApp);
    }

    @PutMapping(path = "/{id}")
    public FavouriteApp updateFavouriteApp(@RequestBody FavouriteApp newFavouriteApp, @PathVariable("id") Long id) {
        FavouriteApp favouriteApp = favouriteAppRepository.findById(id).orElse(null);

        if (favouriteApp != null) {
            favouriteApp.setAppId(newFavouriteApp.getAppId()!=null?newFavouriteApp.getAppId():favouriteApp.getAppId());

            return favouriteAppRepository.save(favouriteApp);
        }
        else return null;
    }

    @DeleteMapping(path = "/{id}")
    public FavouriteApp deleteFavouriteApp(@PathVariable("id") Long id) {
        FavouriteApp favouriteApp = favouriteAppRepository.findById(id).orElse(null);

        if (favouriteApp != null) {
            favouriteAppRepository.deleteById(id);
            return favouriteApp;
        }
        else return null;
    }
}
