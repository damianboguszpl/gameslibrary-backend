package pl.pollub.gameslibrary.Services;

import org.springframework.http.ResponseEntity;
import pl.pollub.gameslibrary.Exceptions.Exceptions.IncorrectRequestDataException;
import pl.pollub.gameslibrary.Exceptions.Exceptions.RoleNotFoundException;
import pl.pollub.gameslibrary.Models.Role;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;

public interface RoleService {
    Iterable<Role> getAll();
    Role getById(Long id);
    Role edit(Role newRole, Long id) throws IncorrectRequestDataException, RoleNotFoundException;
    ResponseEntity<DetailedResponse> add(Role role);
    ResponseEntity<DetailedResponse> delete(Long id) throws RoleNotFoundException;
    ResponseEntity<DetailedResponse> addRoleToUser(String email, String roleName);
    ResponseEntity<DetailedResponse> delRoleFromUser(String email, String roleName);
}
