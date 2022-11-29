package pl.pollub.gameslibrary.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Repositories.AppRepository;
import pl.pollub.gameslibrary.Repositories.ReviewRepository;
import pl.pollub.gameslibrary.Repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppRepository appRepository;

    public ResponseEntity<DetailedResponse> add(Long appId, String userEmail, String textReview, Integer rating) {
        if(appId != null && userEmail != null && textReview != null && rating != null) {
            Optional<App> appOptional = appRepository.findById(appId);
            User user = userRepository.findByEmail(userEmail);

            if(user == null)
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new DetailedResponse("USER_NOT_FOUND", "Specified User does not exist.", null));

            if(appOptional.isPresent()) {
                App app = appOptional.get();
                Review existingReview = reviewRepository.findByUserAndApp(user, app);
                if(existingReview == null) {
                    Review review = new Review(null, textReview, rating, user, app);
                    reviewRepository.save(review);
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(new DetailedResponse("NEW_REVIEW_APP_CREATED", "New Review has been created.", null));
                }
                else return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new DetailedResponse("REVIEW_ALREADY_EXISTS", "Review already exists.", null));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new DetailedResponse("APP_NOT_FOUND", "Specified App does not exist.", null));
            }
        }
        else return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Request does not contain required data.", null));
    }

    public ResponseEntity<DetailedResponse> edit(Long id, String textReview, Integer rating) {
        Review review = reviewRepository.findById(id).orElse(null);

        if(review == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new DetailedResponse("REVIEW_NOT_FOUND", "Review does not exist.", null));

        if (textReview != null || rating != null) {
            review.setTextReview(textReview!=null?textReview:review.getTextReview());
            review.setRating(rating!=null?rating:review.getRating());
            reviewRepository.save(review);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DetailedResponse("REVIEW_UPDATED", "Review has been updated.", review));
        }
        else return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Request does not contain required data.", null));
    }

    public ResponseEntity<DetailedResponse> del(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);

        if (review != null) {
            reviewRepository.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DetailedResponse("REVIEW_DELETED", "Review has been deleted.", null));
        }
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new DetailedResponse("REVIEW_NOT_FOUND", "Review does not exist.", null));
    }

    public List<Review> getAll() {
        List<Review> reviews = (List<Review>) reviewRepository.findAll();
        if(!reviews.isEmpty()) return reviews;
        else return null;
    }

    public Review getById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> getByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return reviewRepository.findByUserIs(user);
        }
        else return null;
    }

    public Review getByUserIdAndAppId(Long userId, Long appId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<App> appOptional = appRepository.findById(appId);
        if(userOptional.isPresent() && appOptional.isPresent()) {
            User user = userOptional.get();
            App app = appOptional.get();
            return reviewRepository.findByUserAndApp(user, app);
        }
        else return null;
    }

    public List<Review> getByAppId(Long appId) {
        Optional<App> appOptional = appRepository.findById(appId);
        if(appOptional.isPresent()) {
            App app = appOptional.get();
            return reviewRepository.findByApp(app);
        }
        else return null;
    }
}
