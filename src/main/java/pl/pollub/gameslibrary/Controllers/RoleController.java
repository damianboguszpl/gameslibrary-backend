package pl.pollub.gameslibrary.Controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.Role;
import pl.pollub.gameslibrary.Models.Utility.DetailedResponse;
import pl.pollub.gameslibrary.Services.RoleService;

import java.util.Objects;

@RestController
@RequestMapping(path="/role")
@RequiredArgsConstructor
public class RoleController {
    @Autowired
    private final RoleService roleService;

    @PostMapping(path = "")
    public ResponseEntity<DetailedResponse> addRole(@RequestBody Role role) {
        ResponseEntity<DetailedResponse> newRole = roleService.add(role);
        return Objects.requireNonNullElseGet(newRole, () -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse>  updateRole(@RequestBody Role newRole, @PathVariable("id") Long id) {
//        Role role = roleService.edit(newRole, id);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new DetailedResponse("ROLE_UPDATED", "Role details have been updated.", role));
        return roleService.edit(newRole, id);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DetailedResponse> deleteRole(@PathVariable("id") Long id) {
        return roleService.delete(id);
    }

    @GetMapping(path = "")
    public ResponseEntity<Iterable<Role>> getAllRoles() {
        return ResponseEntity.ok().body(roleService.getAll());
    }

    @GetMapping (path = "/{id}")
    public Role getRoleById(@PathVariable("id") Long id) {
        return roleService.getById(id);
    }

    @PostMapping(path = "/user/addrole")
    public ResponseEntity<DetailedResponse> addRoleToUser(@RequestBody RoleToUserForm form) {
        return roleService.addRoleToUser(form.getEmail(),form.getRoleName());
    }

    @PostMapping(path = "/user/delrole")
    public ResponseEntity<DetailedResponse> delRoleFromUser(@RequestBody RoleToUserForm form) {
        return roleService.delRoleFromUser(form.getEmail(),form.getRoleName());
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }
}

@Data
class RoleToUserForm {
    private String email;
    private String roleName;
}