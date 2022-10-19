package pl.pollub.gameslibrary.Models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@ToString
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}
