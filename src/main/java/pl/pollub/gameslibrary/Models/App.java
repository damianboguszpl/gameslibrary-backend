package pl.pollub.gameslibrary.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Data
@ToString
@Component
@NoArgsConstructor
@AllArgsConstructor
public class App {
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false,columnDefinition="TEXT")
    private String description;

    @Column(nullable = false,columnDefinition="TEXT")
    private String shortDescription;

    @Column(nullable = false)
    private String screenshotLink;

    @Column(nullable = false,columnDefinition="TEXT")
    private String developers;

    @Column(nullable = false,columnDefinition="TEXT")
    private String publishers;
}
