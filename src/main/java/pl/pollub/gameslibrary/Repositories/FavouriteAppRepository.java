package pl.pollub.gameslibrary.Repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Models.FavouriteApp;
import pl.pollub.gameslibrary.Models.User;

import java.util.List;

@Repository
public interface FavouriteAppRepository extends CrudRepository<FavouriteApp, Long> {
    List<FavouriteApp> findByUserIs(User user);
    FavouriteApp findByUserAndApp(User user, App app);
}
