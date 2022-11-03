package pl.pollub.gameslibrary.Services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
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
    @Autowired
    public User add(User user) {
        if (user != null) {
            if(user.getPassword() != null) {
                String password = user.getPassword();
                String bcryptHashString = BCrypt.withDefaults().hashToString(10, password.toCharArray());
                user.setPassword(bcryptHashString);

                return userRepository.save(user);
            }
            else return null;
        }
        else return null;
    }

    public User del(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            userRepository.deleteById(id);
            return user;
        }
        else return null;
    }
}
