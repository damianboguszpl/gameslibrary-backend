package pl.pollub.gameslibrary.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Services.UserService;

@RestController
@RequestMapping(path="/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping (path = "/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping(path = "")
    public User addUser(@RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping(path = "/{id}")
    public User updateUser(@RequestBody User newUser, @PathVariable("id") Long id) {
        return userService.edit(newUser, id);
    }

    @DeleteMapping(path = "/{id}")
    public User deleteUser(@PathVariable("id") Long id) {
        return userService.del(id);
    }
}
