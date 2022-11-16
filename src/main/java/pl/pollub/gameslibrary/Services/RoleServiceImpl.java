package pl.pollub.gameslibrary.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.Role;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Repositories.RoleRepository;
import pl.pollub.gameslibrary.Repositories.UserRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    public Iterable<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role edit(Role newRole, Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            role.setName(newRole.getName()!=null?newRole.getName():role.getName());
            return roleRepository.save(role);
        }
        else return null;
    }
    @Autowired
    public Role add(Role role) {
        if (role != null) {
            Role existingRole = roleRepository.findByName(role.getName());

            if (existingRole != null) {
                log.info("Role: {} already exists", role.getName());
                return null;
            }
            else {
                if(role.getName() != null) {
                    log.info("Saving new Role: {}", role.getName());
                    return roleRepository.save(role);
                }
                else return null;
            }

        }
        else return null;
    }

    public Role del(Long id) {
        Role role = roleRepository.findById(id).orElse(null);

        if (role != null) {
            roleRepository.deleteById(id);
            return role;
        }
        else return null;
    }

    public void addRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);

        if(user.getRoles().contains(role)) {
            log.info("User: {} already has Role: {}", email, roleName);
        }
        else {
            log.info("Adding Role: {} to User: {}", roleName, email);
            user.getRoles().add(role);
        }
    }

    public void delRoleFromUser(String email, String roleName) {
        User user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        if(user.getRoles().contains(role)) {
            log.info("Deleting Role: {} from User: {}", roleName, email);
            user.getRoles().remove(role);
        }
        log.info("User: {} have not got Role: {}", email, roleName);
    }
}
