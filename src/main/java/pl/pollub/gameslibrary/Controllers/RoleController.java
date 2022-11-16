package pl.pollub.gameslibrary.Controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.pollub.gameslibrary.Models.Role;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Services.RoleService;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/role")
@RequiredArgsConstructor
public class RoleController {
    @Autowired
    private final RoleService roleService;

    @GetMapping(path = "")
    public ResponseEntity<Iterable<Role>> getAllRoles() {
        return ResponseEntity.ok().body(roleService.getAll());
    }

    @GetMapping (path = "/{id}")
    public Role getRoleById(@PathVariable("id") Long id) {
        return roleService.findById(id);
    }

    @PostMapping(path = "")
    public ResponseEntity<Role> addRole(@Valid @RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/roles").toUriString());
        Role newRole = roleService.add(role);
        if (newRole != null) {
            return ResponseEntity.created(uri).body(newRole);
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping(path = "/user")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        roleService.addRoleToUser(form.getEmail(),form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/user/role/del")
    public ResponseEntity<?> delRoleFromUser(@RequestBody RoleToUserForm form) {
        roleService.delRoleFromUser(form.getEmail(),form.getRoleName());
        return ResponseEntity.ok().build();
    }


    @PutMapping(path = "/{id}")
    public Role updateRole(@RequestBody Role newRole, @PathVariable("id") Long id) {
        return roleService.edit(newRole, id);
    }

    @DeleteMapping(path = "/{id}")
    public Role deleteRole(@PathVariable("id") Long id) {
        return roleService.del(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

@Data
class RoleToUserForm {
    private String email;
    private String roleName;
}