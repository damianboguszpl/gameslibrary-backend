package pl.pollub.gameslibrary.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Repositories.UserRepository;

@RestController
@RequestMapping(path="/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping (path = "/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping(path = "")
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping(path = "/{id}")
    public User updateUser(@RequestBody User newUser, @PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            user.setLogin(newUser.getLogin()!=null?newUser.getLogin():user.getLogin());
            user.setEmail(newUser.getEmail()!=null?newUser.getEmail():user.getEmail());
            if(newUser.getPassword() != null) {
                // inline if overrides hash
                user.setPassword(newUser.getPassword());
            }

            return userRepository.save(user);
        }
        else return null;
    }

    @DeleteMapping(path = "/{id}")
    public User deleteUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            userRepository.deleteById(id);
            return user;
        }
        else return null;
    }
}
