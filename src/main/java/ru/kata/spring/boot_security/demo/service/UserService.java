package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void saveUser(User user);

    User getUserById(Long id);

    void updateUser(User user, List<Long> roleIds);

    void deleteUser(Long id);

    User findByUsername(String username);

    User findfindByUserEmail(String email);

}
