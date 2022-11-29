package pl.pollub.gameslibrary.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pollub.gameslibrary.Models.App;

@Repository
public interface AppRepository extends CrudRepository<App, Long> {
    Iterable<App> findByType(String type);

    @Query("SELECT app FROM App app WHERE app.type != 'game' AND app.type != 'dlc'")
    Iterable<App> findByTypeEqualsOther();
}
