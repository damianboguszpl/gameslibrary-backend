package pl.pollub.gameslibrary.Models;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Data
@ToString
@Component
public class FavouriteApp {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="appId",referencedColumnName="id")
    private App appId;

    @ManyToOne
    @JoinColumn(name="userId",referencedColumnName="id")
    private User userId;
}
