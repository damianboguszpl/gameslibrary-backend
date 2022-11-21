package pl.pollub.gameslibrary.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@ToString
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String textReview;

    @Column(nullable = false)
    private Integer rating;

    @JsonIgnore             // to refactor ... json responses do not contain user_id contained in user
    @ManyToOne()
    @JoinColumn(name="userId")
//    @JsonProperty("userId")
    private User user;

    @ManyToOne()
    @JoinColumn(name="appId",referencedColumnName="id")
    private App app;

}
