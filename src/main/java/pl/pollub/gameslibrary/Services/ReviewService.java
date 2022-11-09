package pl.pollub.gameslibrary.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Repositories.ReviewRepository;
import pl.pollub.gameslibrary.Repositories.UserRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    public Iterable<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public Review edit(Review newReview, Long id) {
        Review review = reviewRepository.findById(id).orElse(null);

        if (review != null) {
            review.setTextReview(newReview.getTextReview()!=null?newReview.getTextReview():review.getTextReview());
            review.setRating(newReview.getRating()!=null?newReview.getRating():review.getRating());
            review.setAppId(newReview.getAppId()!=null?newReview.getAppId():review.getAppId());
//            review.setUserId(newReview.getUserId()!=null?newReview.getUserId():review.getUserId());
//            review.setUser(newReview.getUser()!=null?newReview.getUser():review.getUser());
            return reviewRepository.save(review);
        }
        else return null;
    }

    @Autowired
    public Review add(Review review) {
//        System.out.println(review.toString());
//        if(review.getAppId() != null && review.getTextReview() != null && review.getRating() != null && review.getUserId() != null) {
//        if(review.getAppId() != null && review.getTextReview() != null && review.getRating() != null && review.getUser() != null) {
            if(review.getAppId() != null && review.getTextReview() != null && review.getRating() != null) {
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
}
