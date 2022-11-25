package pl.pollub.gameslibrary.Services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Exceptions.Exceptions.*;
import pl.pollub.gameslibrary.Models.Utility.DetailedUserResponse;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public ResponseEntity<DetailedUserResponse> add(User user) {
        if (user != null) {
            if (user.getEmail() == null)
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedUserResponse("INCORRECT_REQUEST_DATA", "E-mail jest pusty.", null));
            if(user.getLogin() != null) {
                String loginValidationRegex = "^[a-zA-Z0-9._-]{3,}$";
                Pattern pattern = Pattern.compile(loginValidationRegex);
                Matcher matcher = pattern.matcher(user.getLogin());
                if (!matcher.matches()) return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedUserResponse("LOGIN_NOT_VALID", "Login nie spełnia zasad poprawności.", null));
            }
            else return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedUserResponse("INCORRECT_REQUEST_DATA", "Login jest pusty.", null));

            User existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser != null)
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new DetailedUserResponse("EMAIL_ALREADY_TAKEN", "Podany adres e-mail wykorzystuje istniejące już konto.", null));
            else {
                existingUser = userRepository.findByLogin(user.getLogin());
                if (existingUser != null)
                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(new DetailedUserResponse("LOGIN_ALREADY_TAKEN", "Podany login jest już zajęty.", null));
            }

            if(user.getPassword() != null) {
                String passwordValidationRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
                Pattern pattern = Pattern.compile(passwordValidationRegex);
                Matcher matcher = pattern.matcher(user.getPassword());

                if(matcher.matches()) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userRepository.save(user);
                    roleService.addRoleToUser(user.getEmail(), "USER_ROLE");
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(new DetailedUserResponse("USER_REGISTERED_SUCCESSFULLY", "", null));
                }
                else
                    return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedUserResponse("PASSWORD_NOT_VALID", "Hasło nie spełnia zasad poprawności.", null));
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedUserResponse("INCORRECT_REQUEST_DATA", "Hasło jest puste.", null));
        }
        else
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedUserResponse("INCORRECT_REQUEST_DATA", "Zapytanie nie zawiera poprawnych danych.", null));
    }

    public User edit(User newUser, Long id) throws UserNotFoundException, IncorrectRequestDataException {
        if(newUser.getEmail() != null || newUser.getLogin() != null || newUser.getPassword() != null) {
            User user = userRepository.findById(id).orElse(null);

            if (user != null) {
                user.setLogin(newUser.getLogin()!=null?newUser.getLogin():user.getLogin());
                user.setEmail(newUser.getEmail()!=null?newUser.getEmail():user.getEmail());

                if(newUser.getPassword() != null) {
                    String password = newUser.getPassword();
                    String bcryptHashString = BCrypt.withDefaults().hashToString(10, password.toCharArray());
                    user.setPassword(bcryptHashString);
                }
                userRepository.save(user);
                return user;
            }
            else
                throw new UserNotFoundException();
        }
        else {
            throw new IncorrectRequestDataException();
        }
    }

    public ResponseEntity<DetailedUserResponse> delete(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DetailedUserResponse("USER_DELETED", "Użytkownik został usunięty.", null));
        }
        else
            throw new UserNotFoundException();
    }

    public List<User> getAll(){
        List<User> users = (List<User>) userRepository.findAll();
        if(!users.isEmpty())
            return users;
        else
            return null;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getById(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null)
            throw new UsernameNotFoundException("User not found");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }
}