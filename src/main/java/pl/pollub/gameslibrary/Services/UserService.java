package pl.pollub.gameslibrary.Services;

import pl.pollub.gameslibrary.Models.User;

public interface UserService {
    Iterable<User> getAll();
    User getById(Long id);
    User getByEmail(String email);
    User edit(User newUser, Long id);
    User add(User user);
    User delete(Long id);
}
