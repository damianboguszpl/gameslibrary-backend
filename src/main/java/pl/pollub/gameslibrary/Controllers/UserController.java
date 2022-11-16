package pl.pollub.gameslibrary.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Services.UserService;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/user")
public class UserController {
//    @Autowired
    private final UserService userService;

    @GetMapping(path = "")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAll());
    }

    @GetMapping (path = "/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @GetMapping (path = "/email/{email}")
    public User getUserByEmail(@PathVariable("email") String email) {
        return userService.getByEmail(email);
    }

    @PostMapping(path = "")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").toUriString());
        User newUser = userService.add(user);
        if (newUser != null) {
            return ResponseEntity.created(uri).body(newUser);
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PutMapping(path = "/{id}")
    public User updateUser(@RequestBody User newUser, @PathVariable("id") Long id) {
        return userService.edit(newUser, id);
    }

    @DeleteMapping(path = "/{id}")
    public User deleteUser(@PathVariable("id") Long id) {
        return userService.delete(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
