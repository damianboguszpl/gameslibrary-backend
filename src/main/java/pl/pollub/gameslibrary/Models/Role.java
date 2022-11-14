package pl.pollub.gameslibrary.Models;

import lombok.*;
import org.springframework.stereotype.Component;
import javax.persistence.*;

@Entity
@Data
@ToString
@Component
@NoArgsConstructor @AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
