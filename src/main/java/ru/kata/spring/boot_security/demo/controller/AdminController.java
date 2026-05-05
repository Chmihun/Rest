package ru.kata.spring.boot_security.demo.controller;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/admin")
    public String hello(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles",roleService.getAllRoles());
        return "firstPage";
    }

    @GetMapping(value = "/")
    public String hello() {
        return "redirect:/login";
    }

    @GetMapping(value = "/admin/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {

        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles()); // Прямая передача

        return "edit";
    }
    @PostMapping("/admin/update")
    public String update(@ModelAttribute("user") User user, @RequestParam(value = "roles", required = false) List<Long> roleIds) {
        userService.updateUser(user, roleIds);
        return "redirect:/admin";

    }

    @GetMapping("/admin/add")
    public String addNewUser(Model model) {
        User newUser = new User();
        newUser.setRoles(new HashSet<>());  // ← добавить
        model.addAttribute("user", newUser);
        model.addAttribute("allRoles", roleService.getAllRoles());

        return "addNewUser";
    }

   @PostMapping("/admin/add")
   public String create(@ModelAttribute("user") User user,
                        @RequestParam(value = "roles", required = false) List<Long> roleIds,
                        Model model) {

       if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
           model.addAttribute("error", "Пароль обязателен");
           model.addAttribute("allRoles", roleService.getAllRoles());
           return "addNewUser";  // Вернуть на страницу добавления
       }

       if (roleIds == null || roleIds.isEmpty()) {
           model.addAttribute("error", "Выберите роль");
           model.addAttribute("allRoles", roleService.getAllRoles());
           model.addAttribute("user", user);
           return "addNewUser";  // Вернуть на страницу добавления
       }

       Set<Role> roles = roleIds.stream()
               .map(roleService::getRoleById)
               .collect(Collectors.toSet());
       user.setRoles(roles);

       user.setPassword(passwordEncoder.encode(user.getPassword()));

       userService.saveUser(user);
       return "redirect:/admin";
   }
    @GetMapping("/admin/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}

