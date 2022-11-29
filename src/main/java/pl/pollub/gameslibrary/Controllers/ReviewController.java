package pl.pollub.gameslibrary.Controllers;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Models.ReviewDto;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Services.ReviewService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path="/review")
public class ReviewController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ReviewService reviewService;

    @PostMapping(path = "")
    public ResponseEntity<DetailedResponse> addReview(@RequestBody ReviewForm form) {

        ResponseEntity<DetailedResponse> newReview = reviewService.add(form.getAppId(), form.getUserEmail(), form.getTextReview(), form.getRating());
        return Objects.requireNonNullElseGet(newReview, () -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> updateReview(@RequestBody SimplifiedReviewForm form, @PathVariable("id") Long id) {
        ResponseEntity<DetailedResponse> responseEntity = reviewService.edit(id, form.getTextReview(), form.getRating());
//        ResponseEntity<DetailedResponse> result =
//        return  reviewService.edit(id, form.getTextReview(), form.getRating());
        Review newReview = (Review) Objects.requireNonNull(responseEntity.getBody()).getData();
        if(newReview != null) {
            ReviewDto reviewDto = modelMapper.map(newReview,ReviewDto.class);
            return ResponseEntity
                    .status(responseEntity.getStatusCode())
                    .body(new DetailedResponse(responseEntity.getBody().getCode(), responseEntity.getBody().getMessage(), reviewDto));
        }
        else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> deleteReview(@PathVariable("id") Long id) {
        return reviewService.del(id);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<Review> reviews = reviewService.getAll();
        if (reviews != null) {
            Type listType = new TypeToken<List<ReviewDto>>(){}.getType();
            List<ReviewDto> reviewsDto = modelMapper.map(reviews,listType);
            return ResponseEntity.ok().body(reviewsDto);
        }
        else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping (path = "/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable("id") Long id) {
        Review review = reviewService.getById(id);
        if (review != null) {
            ReviewDto reviewDto = modelMapper.map(review,ReviewDto.class);
            return ResponseEntity.ok().body(reviewDto);
        }
        else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping (path = "/user/{id}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUserEmail(@PathVariable("id") Long id) {
        List<Review> reviews = reviewService.getByUserId(id);
        if (!reviews.isEmpty()) {
            Type listType = new TypeToken<List<ReviewDto>>(){}.getType();
            List<ReviewDto> reviewsDto = modelMapper.map(reviews,listType);
            return ResponseEntity.ok().body(reviewsDto);
        }
        else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping (path = "/user/{userId}/app/{appId}")
    public ResponseEntity<ReviewDto> getReviewByUserEmailAndAppId(@PathVariable("userId") Long userId, @PathVariable("appId") Long appId) {
        Review review = reviewService.getByUserIdAndAppId(userId, appId);
        if (review != null) {
            ReviewDto reviewDto = modelMapper.map(review,ReviewDto.class);
            return ResponseEntity.ok().body(reviewDto);
        }
        else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping (path = "/app/{id}")
    public ResponseEntity<Iterable<ReviewDto>> getReviewsByAppId(@PathVariable("id") Long id) {
        Iterable<Review> reviews = reviewService.getByAppId(id);
        if (reviews != null) {
            Type listType = new TypeToken<List<ReviewDto>>(){}.getType();
            List<ReviewDto> reviewsDto = modelMapper.map(reviews,listType);
            return ResponseEntity.ok().body(reviewsDto);
        }
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