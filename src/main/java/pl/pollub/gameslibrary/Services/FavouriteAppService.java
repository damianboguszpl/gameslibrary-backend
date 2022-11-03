package pl.pollub.gameslibrary.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.FavouriteApp;
import pl.pollub.gameslibrary.Repositories.FavouriteAppRepository;

@Service
public class FavouriteAppService {
    @Autowired
    private FavouriteAppRepository favouriteAppRepository;

    public Iterable<FavouriteApp> findAll() {
        return favouriteAppRepository.findAll();
    }

    public FavouriteApp findById(Long id) {
        return favouriteAppRepository.findById(id).orElse(null);
    }

    public FavouriteApp edit(FavouriteApp newFavouriteApp, Long id) {
        FavouriteApp favouriteApp = favouriteAppRepository.findById(id).orElse(null);

        if (favouriteApp != null) {
            favouriteApp.setAppId(newFavouriteApp.getAppId()!=null?newFavouriteApp.getAppId():favouriteApp.getAppId());
            favouriteApp.setUserId(newFavouriteApp.getUserId()!=null?newFavouriteApp.getUserId():favouriteApp.getUserId());
            return favouriteAppRepository.save(favouriteApp);
        }
        else return null;
    }

    @Autowired
    public FavouriteApp add(FavouriteApp favouriteApp) {
        if(favouriteApp.getAppId() != null && favouriteApp.getUserId() != null) {
            return favouriteAppRepository.save(favouriteApp);
        }
        else return null;
    }

    public FavouriteApp del(Long id) {
        FavouriteApp favouriteApp = favouriteAppRepository.findById(id).orElse(null);

        if (favouriteApp != null) {
            favouriteAppRepository.deleteById(id);
            return favouriteApp;
        }
        else return null;
    }
}
