package pl.pollub.gameslibrary.Controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Services.ReviewService;

import java.util.List;

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

    @GetMapping (path = "/useremail/{email}")   // working
    public ResponseEntity<List<Review>> getReviewsByUserEmail(@PathVariable("email") String email) {
        List<Review> reviews = reviewService.getByUserEmail(email);
        if (reviews != null) {
            return ResponseEntity.ok().body(reviews);
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }


    @PostMapping(path = "")
    public Review addReview(@RequestBody ReviewForm form) {
        return reviewService.add(form.getAppId(), form.getUserEmail(), form.getTextReview(), form.getRating());
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

@Data
class ReviewForm {
    private Long appId;
    private String userEmail;
    private String textReview;
    private Integer rating;
}