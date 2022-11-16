package pl.pollub.gameslibrary.Services;

import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.FavouriteApp;

@Service
public interface FavouriteAppService {
    Iterable<FavouriteApp> findAll();
    FavouriteApp findById(Long id);
    FavouriteApp edit(FavouriteApp newFavouriteApp, Long id);
    FavouriteApp add(FavouriteApp favouriteApp);
    FavouriteApp del(Long id);
}
