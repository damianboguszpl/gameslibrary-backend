package pl.pollub.gameslibrary.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Repositories.UserRepository;

@Service
public class AuthenticatedUserService {

    @Autowired
    private UserRepository userRepository;

    public boolean hasId(Long id){
        String email =  SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        return user.getId().equals(id);
    }
}
