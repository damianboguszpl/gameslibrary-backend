package pl.pollub.gameslibrary.Repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pollub.gameslibrary.Models.Review;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

}
