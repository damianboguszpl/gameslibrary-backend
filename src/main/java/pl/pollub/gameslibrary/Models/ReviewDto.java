package pl.pollub.gameslibrary.Models;

import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private String textReview;
    private Integer rating;
    private UserDto user;
    private App app;
}
