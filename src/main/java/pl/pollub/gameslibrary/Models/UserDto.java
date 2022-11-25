package pl.pollub.gameslibrary.Models;

import lombok.Data;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class UserDto {
    private Long id;
    private String login;
    private String email;
    private Collection<Role> roles = new ArrayList<>();
}
