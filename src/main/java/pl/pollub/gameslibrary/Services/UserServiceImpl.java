package pl.pollub.gameslibrary.Services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.Review;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Repositories.RoleRepository;
import pl.pollub.gameslibrary.Repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            log.info("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        else {
            log.info("User: {} found", email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }

    public Iterable<User> getAll() {
        log.info("Fetching all the Users");
        return userRepository.findAll();
    }

    public User getByEmail(String email) {
        log.info("Fetching User: {}", email);
        return userRepository.findByEmail(email);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Autowired
    public User add(User user) {
        if (user != null) {
            User existingUser = userRepository.findByEmail(user.getEmail());

            if (existingUser != null) {
                log.info("Given User already exists");
                return null;
            }
            else {
                if(user.getPassword() != null) {
//                    String password = user.getPassword();
//                    String bcryptHashString = BCrypt.withDefaults().hashToString(10, password.toCharArray());
//                    user.setPassword(bcryptHashString);
                    user.setPassword(passwordEncoder.encode(user.getPassword()));

                    log.info("Saving new User");
                    User newUser = userRepository.save(user);
                    roleService.addRoleToUser(user.getEmail(), "USER_ROLE");
                    return newUser;
                }
                else return null;
            }

        }
        else return null;
    }

    public User edit(User newUser, Long id) {
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

    public User delete(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            userRepository.deleteById(id);
            return user;
        }
        else return null;
    }


}
