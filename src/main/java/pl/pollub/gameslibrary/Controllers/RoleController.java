package pl.pollub.gameslibrary.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.gameslibrary.Models.Role;
import pl.pollub.gameslibrary.Services.RoleService;

@RestController
@RequestMapping(path="/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping(path = "")
    public Iterable<Role> getAllRoles() {
        return roleService.findAll();
    }

    @GetMapping (path = "/{id}")
    public Role getRoleById(@PathVariable("id") Long id) {
        return roleService.findById(id);
    }

    @PostMapping(path = "")
    public Role addRole(@RequestBody Role role) {
        return roleService.add(role);
    }

    @PutMapping(path = "/{id}")
    public Role updateRole(@RequestBody Role newRole, @PathVariable("id") Long id) {
        return roleService.edit(newRole, id);
    }

    @DeleteMapping(path = "/{id}")
    public Role deleteRole(@PathVariable("id") Long id) {
        return roleService.del(id);
    }
}
