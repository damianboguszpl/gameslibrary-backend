package pl.pollub.gameslibrary.Services;

import org.springframework.http.ResponseEntity;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Models.User;

public interface UserService {
    ResponseEntity<DetailedResponse> add(User user);
    ResponseEntity<DetailedResponse> edit(User newUser, Long id);
    ResponseEntity<DetailedResponse> delete(Long id);
    Iterable<User> getAll();
    User getById(Long id);
    User getByEmail(String email);
}
