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
import pl.pollub.gameslibrary.Models.FavouriteApp;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
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
    private final ReviewService reviewService;
    private final FavouriteAppService favouriteAppService;

    @Autowired
    public ResponseEntity<DetailedResponse> add(User user) {
        if (user != null) {
            if (user.getEmail() == null)
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "E-mail parameter not specified.", null));
            if(user.getLogin() != null) {
                String loginValidationRegex = "^[a-zA-Z0-9._-]{3,}$";
                Pattern pattern = Pattern.compile(loginValidationRegex);
                Matcher matcher = pattern.matcher(user.getLogin());
                if (!matcher.matches()) return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedResponse("LOGIN_NOT_VALID", "Login does not match validation rules.", null));
            }
            else return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Login parameter not specified.", null));

            User existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser != null)
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new DetailedResponse("EMAIL_ALREADY_TAKEN", "Given E-mail address is already used by existing User.", null));
            else {
                existingUser = userRepository.findByLogin(user.getLogin());
                if (existingUser != null)
                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(new DetailedResponse("LOGIN_ALREADY_TAKEN", "Given Login is already taken.", null));
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
                            .body(new DetailedResponse("NEW_USER_REGISTERED", "New User account has been created.", null));
                }
                else return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedResponse("PASSWORD_NOT_VALID", "Password does not match validation rules.", null));
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Password parameter not specified.", null));
        }
        else return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Request does not contain required data.", null));
    }

    public ResponseEntity<DetailedResponse> edit(User newUser, Long id) {
        if(newUser.getEmail() != null || newUser.getLogin() != null || newUser.getPassword() != null) {
            User user = userRepository.findById(id).orElse(null);

            if (user != null) {
                User existingUser = userRepository.findByLogin(newUser.getLogin());
                if (existingUser != null)
                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(new DetailedResponse("LOGIN_ALREADY_TAKEN", "Given Login is already taken.", null));
                user.setLogin(newUser.getLogin()!=null?newUser.getLogin():user.getLogin());
                user.setEmail(newUser.getEmail()!=null?newUser.getEmail():user.getEmail());

                if(newUser.getPassword() != null) {
                    String password = newUser.getPassword();
                    String bcryptHashString = BCrypt.withDefaults().hashToString(10, password.toCharArray());
                    user.setPassword(bcryptHashString);
                }
                userRepository.save(user);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new DetailedResponse("USER_UPDATED", "User has been updated.", user));
            }
            else return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new DetailedResponse("USER_NOT_FOUND", "Specified User does not exist.", null));
        }
        else return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Request does not contain required data.", null));
    }

    public ResponseEntity<DetailedResponse> delete(Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            List<Review> userReviews = reviewService.getByUserId(user.getId());
            userReviews.forEach(review -> reviewService.del(review.getId()));
            List<FavouriteApp> userFavouriteApps = (List<FavouriteApp>) favouriteAppService.getByUserId(user.getId());
            userFavouriteApps.forEach(favouriteApp -> favouriteAppService.delete(favouriteApp.getId()));

            userRepository.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DetailedResponse("USER_DELETED", "User has been deleted.", null));
        }
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new DetailedResponse("USER_NOT_FOUND", "User does not exist.", null));
    }

    public List<User> getAll(){
        List<User> users = (List<User>) userRepository.findAll();
        return !users.isEmpty() ? users : null;
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