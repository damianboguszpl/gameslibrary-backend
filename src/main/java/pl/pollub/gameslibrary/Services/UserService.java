package pl.pollub.gameslibrary.Services;

import org.springframework.http.ResponseEntity;
import pl.pollub.gameslibrary.Models.Utility.DetailedUserResponse;
import pl.pollub.gameslibrary.Exceptions.Exceptions.*;
import pl.pollub.gameslibrary.Models.User;

public interface UserService {
    Iterable<User> getAll();
    User getById(Long id);
    User getByEmail(String email);
    User edit(User newUser, Long id) throws UserNotFoundException, IncorrectRequestDataException;
    ResponseEntity<DetailedUserResponse> add(User user);
    ResponseEntity<DetailedUserResponse> delete(Long id) throws UserNotFoundException;
}
