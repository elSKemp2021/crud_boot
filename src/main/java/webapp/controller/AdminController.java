package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webapp.model.Role;
import webapp.model.User;
import webapp.service.UserService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@Transactional
public class AdminController {

    private  UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/adduser")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/adduser";
    }

    @PostMapping()
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(required = false, name = "ROLE_ADMIN") String ROLE_ADMIN,
                           @RequestParam(required = false, name = "ROLE_USER") String ROLE_USER) {

        Set<Role> roles  = new HashSet<>();

        if (ROLE_ADMIN != null) {
            roles.add(new Role(1L, ROLE_ADMIN));
        }
        if (ROLE_USER != null) {
            roles.add(new Role(2L, ROLE_USER));
        }

        user.setRoles(roles);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edituser")
    public String editUser(Model model,
                           @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUser(id));
        return "admin/edituser";
    }

    @PatchMapping("/{id}")
    public String editUser(@ModelAttribute("user") User user,
                           @RequestParam(required = false, name = "ROLE_ADMIN") String ROLE_ADMIN,
                           @RequestParam(required = false, name = "ROLE_USER") String ROLE_USER) {

        Set<Role> roles  = new HashSet<>();

        if (ROLE_ADMIN != null) {
            roles.add(new Role(1L, ROLE_ADMIN));
        }
        if (ROLE_USER != null) {
            roles.add(new Role(2L, ROLE_USER));
        }

        user.setRoles(roles);
        userService.editUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(userService.getUser(id));
        return "redirect:/admin";
    }
}