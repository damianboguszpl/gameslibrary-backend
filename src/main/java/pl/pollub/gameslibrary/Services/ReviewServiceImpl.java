package pl.pollub.gameslibrary.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppRepository appRepository;

    public Iterable<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> getByUserEmail(String userEmail) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userEmail));

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("user: {}", user.getEmail());
            return reviewRepository.findByUserIs(user);
        }
        else return null;
    }

    public Review edit(Review newReview, Long id) {
        Review review = reviewRepository.findById(id).orElse(null);

        if (review != null) {
            review.setTextReview(newReview.getTextReview()!=null?newReview.getTextReview():review.getTextReview());
            review.setRating(newReview.getRating()!=null?newReview.getRating():review.getRating());
//            review.setAppId(newReview.getAppId()!=null?newReview.getAppId():review.getAppId());
//            review.setUserId(newReview.getUserId()!=null?newReview.getUserId():review.getUserId());
//            review.setUser(newReview.getUser()!=null?newReview.getUser():review.getUser());
            return reviewRepository.save(review);
        }
        else return null;
    }

//    @Autowired
    public Review add(Long appId, String userEmail, String textReview, Integer rating) {
//        System.out.println(review.toString());
//        if(review.getAppId() != null && review.getTextReview() != null && review.getRating() != null && review.getUserId() != null) {
//        if(review.getAppId() != null && review.getTextReview() != null && review.getRating() != null && review.getUser() != null) {
//            if(review.getAppId() != null && review.getTextReview() != null && review.getRating() != null) {
//                return reviewRepository.save(review);
//            }
//            else return null;
        Optional<App> appOptional = appRepository.findById(appId);
        User user = userRepository.findByEmail(userEmail);
        log.info("appId: {}, userEmail: {}, textReview: {}, rating: {}", appId, userEmail, textReview, rating);

        if(appOptional.isPresent() && user != null) {
            App app = appOptional.get();
            if(textReview != null && rating != null ) {
                Review review = new Review(null, textReview, rating, user, app);
                return reviewRepository.save(review);
            }
            else {
                return null;
            }
        } else {
            //app doesn't exist
            return null;
        }
    }

    public Review del(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);

        if (review != null) {
            reviewRepository.deleteById(id);
            return review;
        }
        else return null;
    }
}
