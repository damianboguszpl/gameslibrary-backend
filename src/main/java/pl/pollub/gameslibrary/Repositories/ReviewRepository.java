package pl.pollub.gameslibrary.Repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Models.User;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByUserIs(User user);
    Review findByUserAndApp(User user, App app);
    List<Review> findByApp(App app);
}
