package pl.pollub.gameslibrary.Repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pollub.gameslibrary.Models.FavouriteApp;

@Repository
public interface FavouriteAppRepository extends CrudRepository<FavouriteApp, Long> {

}
