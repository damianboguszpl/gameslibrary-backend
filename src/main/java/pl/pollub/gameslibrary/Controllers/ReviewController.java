package pl.pollub.gameslibrary.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Repositories.ReviewRepository;

@RestController
@RequestMapping(path="/review")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping(path = "")
    public Iterable<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @GetMapping (path = "/{id}")
    public Review getReviewById(@PathVariable("id") Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    @PostMapping(path = "")
    public Review addReview(@RequestBody Review review) {
        return reviewRepository.save(review);
    }

    @PutMapping(path = "/{id}")
    public Review updateReview(@RequestBody Review newReview, @PathVariable("id") Long id) {
        Review review = reviewRepository.findById(id).orElse(null);

        if (review != null) {
            review.setTextReview(newReview.getTextReview()!=null?newReview.getTextReview():review.getTextReview());
            review.setRating(newReview.getRating()!=null?newReview.getRating():review.getRating());
            review.setAppId(newReview.getAppId()!=null?newReview.getAppId():review.getAppId());
            return reviewRepository.save(review);
        }
        else return null;
    }

    @DeleteMapping(path = "/{id}")
    public Review deleteReview(@PathVariable("id") Long id) {
        Review review = reviewRepository.findById(id).orElse(null);

        if (review != null) {
            reviewRepository.deleteById(id);
            return review;
        }
        else return null;
    }
}
