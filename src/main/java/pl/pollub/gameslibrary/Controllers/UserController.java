package pl.pollub.gameslibrary.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Exceptions.Exceptions.*;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Models.UserDto;
import pl.pollub.gameslibrary.Services.UserService;

import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private ModelMapper modelMapper;

    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<DetailedResponse> addUser(@RequestBody User user){
        ResponseEntity<DetailedResponse> newUser = userService.add(user);
        if (newUser != null)
            return newUser;
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> updateUser(@RequestBody User user, @PathVariable("id") Long id) throws UserNotFoundException, IncorrectRequestDataException {
        UserDto userDto = modelMapper.map(userService.edit(user, id),UserDto.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DetailedResponse("USER_UPDATED", "Dane użytkownika zostały zaktualizowane.", userDto));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
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