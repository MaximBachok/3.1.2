package com.example.springboot.controller;

import com.example.springboot.model.User;
import com.example.springboot.service.RoleService;
import com.example.springboot.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class ControllerContr {

    private final UserService userService;
    private final RoleService roleService;

    public ControllerContr(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/index")
    public String getTestPage(){
        return "index";
    }

    @GetMapping("/admin")
    public String getListUsers(Model model){
        model.addAttribute("userList", userService.getAllUser());
        return "users";
    }

    @GetMapping(value="/admin/add")
    public String addUser(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleService.getAllRoles());
        return "addUser";
    }

    @PostMapping(value="/admin/add")
    public String saveUser(@ModelAttribute @Valid User user, BindingResult result,
                           @RequestParam(value = "checked", required = false ) Long[] checked, Model model){
        if(result.hasErrors()){
            return "addUser";
        }
        if (checked == null) {
            user.setOneRole(roleService.getRoleByName("ROLE_USER"));
            userService.addUser(user);
        } else {
            for (Long aLong : checked) {
                if (aLong != null) {
                    user.setOneRole(roleService.getRoleByID(aLong));
                    userService.addUser(user);
                }
            }
        }

        return "redirect:/admin";
    }

    @GetMapping(value="/admin/edit/{id}")
    public String editUserPage(@PathVariable("id") long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleService.getAllRoles());
        return "editUser";
    }
    @PostMapping(value="/admin/edit/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user,
                             BindingResult result,
                             @RequestParam(value = "checked", required = false ) Long[] checked, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "editUser";
        }
        if (checked == null) {
            user.setOneRole(roleService.getRoleByName("ROLE_USER"));
            userService.updateUser(user);
        } else {
            for (Long aLong : checked) {
                if (aLong != null) {
                    user.setOneRole(roleService.getRoleByID(aLong));
                    userService.updateUser(user);
                }
            }
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String getUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                              Model model){
        String name = userDetails.getUsername();
        User user = userService.getByName(name);
        model.addAttribute("user", user);
        return "userPage";
    }

}
