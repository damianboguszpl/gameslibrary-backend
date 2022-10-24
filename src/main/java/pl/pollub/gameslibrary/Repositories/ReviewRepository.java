package pl.pollub.gameslibrary.Repositories;


import org.springframework.data.repository.CrudRepository;
import pl.pollub.gameslibrary.Models.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {

}
