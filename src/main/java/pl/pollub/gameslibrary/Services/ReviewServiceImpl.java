package pl.pollub.gameslibrary.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.App;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Repositories.AppRepository;
import pl.pollub.gameslibrary.Repositories.ReviewRepository;
import pl.pollub.gameslibrary.Repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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


    //    @Autowired
    public Review add(Long appId, String userEmail, String textReview, Integer rating) {
        if(appId != null && userEmail != null && textReview != null && rating != null) {
            Optional<App> appOptional = appRepository.findById(appId);
            User user = userRepository.findByEmail(userEmail);

            if(appOptional.isPresent() && user != null) {
                App app = appOptional.get();
                Review existingReview = reviewRepository.findByUserAndApp(user, app);
                if(existingReview == null) {
                    Review review = new Review(null, textReview, rating, user, app);
                    return reviewRepository.save(review);
                }
                else return null;
            } else {    // app or user doesn't exist
                return null;
            }
        }
        else return null;
    }

    public Review edit(Long id, String textReview, Integer rating) {
        Review review = reviewRepository.findById(id).orElse(null);

        if (review != null && (textReview != null || rating != null)) {
            review.setTextReview(textReview!=null?textReview:review.getTextReview());
            review.setRating(rating!=null?rating:review.getRating());
            return reviewRepository.save(review);
        }
        else return null;
    }

    public Review del(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);

        if (review != null) {
            reviewRepository.deleteById(id);
            return review;
        }
        else return null;
    }

    public List<Review> getAll() {
        List<Review> reviews = (List<Review>) reviewRepository.findAll();
        if(!reviews.isEmpty()) return reviews;
        else return null;
    }

    public Review getById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> getByUserEmail(String userEmail) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userEmail));

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return reviewRepository.findByUserIs(user);
        }
        else return null;
    }

    public Review getByUserEmailAndAppId(String userEmail, Long appId) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userEmail));
        Optional<App> appOptional = appRepository.findById(appId);
        if(userOptional.isPresent() && appOptional.isPresent()) {
            User user = userOptional.get();
            App app = appOptional.get();
            return reviewRepository.findByUserAndApp(user, app);
        }
        else return null;
    }
}
