package pl.pollub.gameslibrary.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;

import java.util.List;

@Service
public interface ReviewService {
    ResponseEntity<DetailedResponse> add(Long appId, String userEmail, String textReview, Integer rating);
    ResponseEntity<DetailedResponse> edit(Long id, String textReview, Integer rating);
    ResponseEntity<DetailedResponse> del(Long id);
    List<Review> getAll();
    Review getById(Long id);
    List<Review> getByUserEmail(String userEmail);
    List<Review> getByAppId(Long appId);
    Review getByUserIdAndAppId(Long userId, Long appId);
}
