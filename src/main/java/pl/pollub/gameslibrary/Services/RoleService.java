package pl.pollub.gameslibrary.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.gameslibrary.Models.Role;
import pl.pollub.gameslibrary.Repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Iterable<Role> findAll() {
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
            if(role.getName() != null) {
                return roleRepository.save(role);
            }
            else return null;
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
}
