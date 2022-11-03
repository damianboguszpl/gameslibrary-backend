package pl.pollub.gameslibrary.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Services.ReviewService;

@RestController
@RequestMapping(path="/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping(path = "")
    public Iterable<Review> getAllReviews() {
        return reviewService.findAll();
    }

    @GetMapping (path = "/{id}")
    public Review getReviewById(@PathVariable("id") Long id) {
        return reviewService.findById(id);
    }

    @PostMapping(path = "")
    public Review addReview(@RequestBody Review review) {
        return reviewService.add(review);
    }

    @PutMapping(path = "/{id}")
    public Review updateReview(@RequestBody Review newReview, @PathVariable("id") Long id) {
        return reviewService.edit(newReview, id);
    }

    @DeleteMapping(path = "/{id}")
    public Review deleteReview(@PathVariable("id") Long id) {
        return reviewService.del(id);
    }
}
