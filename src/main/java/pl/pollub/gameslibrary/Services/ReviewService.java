package pl.pollub.gameslibrary.Services;

import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.Review;

import java.util.List;

@Service
public interface ReviewService {
    Iterable<Review> findAll();
    Review findById(Long id);
    List<Review> getByUserEmail(String userEmail);
    Review edit(Review newReview, Long id);
    Review add(Long appId,String userEmail, String textReview, Integer rating);
    Review del(Long id);
}
