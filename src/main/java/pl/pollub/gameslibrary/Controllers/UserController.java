package pl.pollub.gameslibrary.Controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
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
        String password = user.getPassword();
        String bcryptHashString = BCrypt.withDefaults().hashToString(10, password.toCharArray());
        user.setPassword(bcryptHashString);

        return userRepository.save(user);
    }

    @PutMapping(path = "/{id}")
    public User updateUser(@RequestBody User newUser, @PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            user.setLogin(newUser.getLogin()!=null?newUser.getLogin():user.getLogin());
            user.setEmail(newUser.getEmail()!=null?newUser.getEmail():user.getEmail());

            if(newUser.getPassword() != null) {
                String password = newUser.getPassword();
                String bcryptHashString = BCrypt.withDefaults().hashToString(10, password.toCharArray());
                user.setPassword(bcryptHashString);
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
