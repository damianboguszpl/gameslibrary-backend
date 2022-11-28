package pl.pollub.gameslibrary.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.Role;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Repositories.RoleRepository;
import pl.pollub.gameslibrary.Repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    public ResponseEntity<DetailedResponse> add(Role role) {
        if (role != null) {
            if (role.getName() == null)
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Name parameter not specified.", null));

            Role existingRole = roleRepository.findByName(role.getName());
            if (existingRole != null) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new DetailedResponse("ROLE_ALREADY_EXISTS", "A Role with same name name as given already exists.", null));
            }
            else {
                roleRepository.save(role);
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(new DetailedResponse("NEW_ROLE_CREATED", "New Role has been created.", null));
            }
        }
        else
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Request does not contain required data.", null));
    }

    public ResponseEntity<DetailedResponse> edit(Role newRole, Long id) {
        if(newRole.getName() == null || id == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("INCORRECT_REQUEST_DATA", "Request does not contain required data.", null));
        }
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            role.setName(newRole.getName()!=null?newRole.getName():role.getName());
            roleRepository.save(role);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DetailedResponse("ROLE_UPDATED", "Role has been updated.", role));
        }
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new DetailedResponse("ROLE_NOT_FOUND", "Role does not exist.", null));
    }

    public ResponseEntity<DetailedResponse> delete(Long id){
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            List<User> users = (List<User>) userRepository.findAll();
            User userWithRole = users.stream().filter( user -> user.getRoles().contains(role)).findAny().orElse(null);
            if(userWithRole == null) {
                roleRepository.deleteById(id);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new DetailedResponse("ROLE_DELETED", "Role has been deleted.", null));
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("ROLE_IS_USED", "Role is being used by at least one User and can not be deleted.", null));
        }
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new DetailedResponse("ROLE_NOT_FOUND", "Role does not exist.", null));
    }

    public Iterable<Role> getAll() {
        List<Role> roles = (List<Role>) roleRepository.findAll();
        return !roles.isEmpty() ? roles : null;
    }

    public Role getById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        return roleOptional.orElse(null);
    }

    public ResponseEntity<DetailedResponse> addRoleToUser(String email, String roleName) {
        if (email == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("EMAIL_NOT_SPECIFIED", "Email parameter is not specified.", null));
        if (roleName == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("ROLE_NAME_NOT_SPECIFIED", "RoleName parameter is not specified.", null));

        User user = userRepository.findByEmail(email);
        if(user == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new DetailedResponse("USER_NOT_FOUND", "User has not been found.", null));
        Role role = roleRepository.findByName(roleName);
        if(role == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new DetailedResponse("ROLE_NOT_FOUND", "Role has not been found.", null));

        if(user.getRoles().contains(role)) {
            log.info("User: {} already has Role: {}", email, roleName);
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new DetailedResponse("ROLE_ALREADY_ASSIGNED", "User already has the Role assigned.", null));
        }
        else {
            log.info("Adding Role: {} to User: {}", roleName, email);
            user.getRoles().add(role);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DetailedResponse("ROLE_ASSIGNED", "New Role has been assigned to User.", null));
        }
    }

    public ResponseEntity<DetailedResponse> delRoleFromUser(String email, String roleName) {
        if (email == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("EMAIL_NOT_SPECIFIED", "Email parameter is not specified.", null));
        if (roleName == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new DetailedResponse("ROLE_NAME_NOT_SPECIFIED", "RoleName parameter is not specified.", null));

        User user = userRepository.findByEmail(email);
        if(user == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new DetailedResponse("USER_NOT_FOUND", "User has not been found.", null));
        Role role = roleRepository.findByName(roleName);
        if(role == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new DetailedResponse("ROLE_NOT_FOUND", "Role has not been found.", null));

        if(user.getRoles().contains(role)) {
            log.info("Deleting Role: {} from User: {}", roleName, email);
            user.getRoles().remove(role);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DetailedResponse("ROLE_REVOKED", "The Role has been taken from User.", null));
        }
        else {
            log.info("User: {} already does not have Role: {}", email, roleName);
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new DetailedResponse("ROLE_NOT_OWNED", "User already does not own the Role.", null));
        }
    }
}
