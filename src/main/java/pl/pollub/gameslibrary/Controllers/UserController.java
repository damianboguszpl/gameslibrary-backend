package pl.pollub.gameslibrary.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Models.UserDto;
import pl.pollub.gameslibrary.Services.AuthenticatedUserService;
import pl.pollub.gameslibrary.Services.UserService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private ModelMapper modelMapper;

    private final UserService userService;

    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    @PostMapping(path = "/register")
    public ResponseEntity<DetailedResponse> addUser(@RequestBody User user){
        ResponseEntity<DetailedResponse> newUser = userService.add(user);
        return Objects.requireNonNullElseGet(newUser, () -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> updateUser(@RequestBody User user, @PathVariable("id") Long id) {
        ResponseEntity<DetailedResponse> newUserResponse = userService.edit(user, id);
        User newUser = (User) Objects.requireNonNull(newUserResponse.getBody()).getData();
        UserDto userDto = modelMapper.map(newUser,UserDto.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DetailedResponse("USER_UPDATED", "User has been updated.", userDto));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> deleteUser(@PathVariable("id") Long id){
        return userService.delete(id);
    }

    @GetMapping(path = "")
    public ResponseEntity<Object> getAllUsers() {
        List<User> users = (List<User>) userService.getAll();

        if(!users.isEmpty()) {
            Type listType = new TypeToken<List<UserDto>>(){}.getType();
            List<UserDto> usersDto = modelMapper.map(users,listType);
            return ResponseEntity.ok().body(usersDto);
        }
        else
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("Nie znaleziono żadnych użytkowników.", "", null));
    }

    @GetMapping (path = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ROLE') or @authenticatedUserService.hasId(#id)")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if(user != null) {
            UserDto userDto = modelMapper.map(user,UserDto.class);
            return ResponseEntity.ok().body(userDto);
        }
        else
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("USER_NOT_FOUND", "Szukany użytkownik nie istnieje.", null));
    }

    @GetMapping (path = "/email/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable("email") String email) {
        User user = userService.getByEmail(email);
        if(user != null) {
            UserDto userDto = modelMapper.map(user,UserDto.class);
            return ResponseEntity.ok().body(userDto);
        }
        else
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("USER_NOT_FOUND", "Szukany użytkownik nie istnieje.", null));
    }
}