package pl.pollub.gameslibrary.Models;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@ToString
@Component
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Login is mandatory")
    private String login;

    @Column(nullable = false)
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    private String password;

    @OneToMany(mappedBy = "user", fetch = LAZY)
    private Collection<Review> reviews = new ArrayList<>();

    @ManyToMany(fetch = EAGER) // whenever user will be loaded, his roles will be loaded too
    private Collection<Role> roles = new ArrayList<>();

//    @OneToMany(fetch = EAGER, mappedBy = "user")
//    private Collection<Role> roles = new ArrayList<>();


}
