package pl.pollub.gameslibrary.Services;

import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.Review;

import java.util.List;

@Service
public interface ReviewService {
    Review add(Long appId,String userEmail, String textReview, Integer rating);
    Review edit(Long id, String textReview, Integer rating);
    Review del(Long id);
    List<Review> getAll();
    Review getById(Long id);
    List<Review> getByUserEmail(String userEmail);
    Review getByUserEmailAndAppId(String userEmail, Long appId);
}
