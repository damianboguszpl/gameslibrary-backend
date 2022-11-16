package pl.pollub.gameslibrary.Services;

import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.Review;

@Service
public interface ReviewService {
    Iterable<Review> findAll();
    Review findById(Long id);
    Review edit(Review newReview, Long id);
    Review add(Long appId,String userEmail, String textReview, Integer rating);
    Review del(Long id);
}
