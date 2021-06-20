package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webapp.model.Role;
import webapp.model.User;
import webapp.service.UserService;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/user")
@Transactional
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getUser(Model model,
                          Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user/user";
    }

    @GetMapping("{id}/edituser")
    public String editUser(Model model,
                           @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUser(id));
        return "user/edituser";
    }

    @PatchMapping("/{id}")
    public String editUser(@ModelAttribute("user") User user,
                           @PathVariable("id") long id){

        Set<Role> roles  = new HashSet<>();
        roles.add(new Role(2L, "ROLE_USER"));
        user.setRoles(roles);

        userService.editUser(user);
        return "redirect:/user";
    }
}