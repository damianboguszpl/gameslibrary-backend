package pl.pollub.gameslibrary.Services;

import org.springframework.http.ResponseEntity;
import pl.pollub.gameslibrary.Models.Role;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;

public interface RoleService {
    ResponseEntity<DetailedResponse> add(Role role);
    ResponseEntity<DetailedResponse> edit(Role newRole, Long id);
    ResponseEntity<DetailedResponse> delete(Long id);
    ResponseEntity<DetailedResponse> addRoleToUser(String email, String roleName);
    ResponseEntity<DetailedResponse> delRoleFromUser(String email, String roleName);
    Iterable<Role> getAll();
    Role getById(Long id);
}
