package pl.pollub.gameslibrary.Controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Services.ReviewService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path="/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping(path = "")
    public ResponseEntity<DetailedResponse> addReview(@RequestBody ReviewForm form) {
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/review").toUriString());
//        Review review = reviewService.add(form.getAppId(), form.getUserEmail(), form.getTextReview(), form.getRating());
//        if (review != null) return ResponseEntity.created(uri).body(review);
//        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // other status maybe?

        ResponseEntity<DetailedResponse> newReview = reviewService.add(form.getAppId(), form.getUserEmail(), form.getTextReview(), form.getRating());
        return Objects.requireNonNullElseGet(newReview, () -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> updateReview(@RequestBody SimplifiedReviewForm form, @PathVariable("id") Long id) {
//        return reviewService.edit(newReview, id);
//        Review review = reviewService.edit(id, form.getTextReview(), form.getRating());
//        if (review != null) return ResponseEntity.ok().body(review);
//        else return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // other status maybe?
        return  reviewService.edit(id, form.getTextReview(), form.getRating());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> deleteReview(@PathVariable("id") Long id) {
//        Review review = reviewService.del(id);
//        if (review != null) return ResponseEntity.ok().body(review);
//        else return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // other status maybe?
        return reviewService.del(id);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAll();
        if (reviews != null) return ResponseEntity.ok().body(reviews);
        else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping (path = "/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable("id") Long id) {
        Review review = reviewService.getById(id);
        if (review != null) return ResponseEntity.ok().body(review);
        else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping (path = "/user/{email}")
    public ResponseEntity<List<Review>> getReviewsByUserEmail(@PathVariable("email") String email) {
        List<Review> reviews = reviewService.getByUserEmail(email);
        if (!reviews.isEmpty()) return ResponseEntity.ok().body(reviews);
        else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping (path = "/user/{email}/app/{id}")
    public ResponseEntity<Review> getReviewsByUserEmailAndAppId(@PathVariable("email") String email, @PathVariable("id") Long id) {
        Review reviews = reviewService.getByUserEmailAndAppId(email, id);
        if (reviews != null) return ResponseEntity.ok().body(reviews);
        else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

@Data
class ReviewForm {
    private Long appId;
    private String userEmail;
    private String textReview;
    private Integer rating;
}

@Data
class SimplifiedReviewForm {
    private String textReview;
    private Integer rating;
}