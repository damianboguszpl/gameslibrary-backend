package pl.pollub.gameslibrary.Models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@ToString
public class Review {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long appId;

    @Column(nullable = false)
    private String textReview;

    @Column(nullable = false)
    private Integer rating;

    // + userId

}
