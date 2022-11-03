package pl.pollub.gameslibrary.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Repositories.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

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
            review.setUserId(newReview.getUserId()!=null?newReview.getUserId():review.getUserId());
            return reviewRepository.save(review);
        }
        else return null;
    }

    @Autowired
    public Review add(Review review) {
        if(review.getAppId() != null && review.getTextReview() != null && review.getRating() != null && review.getUserId() != null) {
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
