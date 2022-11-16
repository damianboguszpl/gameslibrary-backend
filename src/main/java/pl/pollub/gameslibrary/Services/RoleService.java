package pl.pollub.gameslibrary.Services;

import pl.pollub.gameslibrary.Models.Role;

public interface RoleService {
    Iterable<Role> getAll();
    Role findById(Long id);
    Role edit(Role newRole, Long id);
    Role add(Role role);
    Role del(Long id);
    void addRoleToUser(String email, String roleName);
    void delRoleFromUser(String email, String roleName);
}
