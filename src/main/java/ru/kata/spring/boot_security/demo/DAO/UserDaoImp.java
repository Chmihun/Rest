package ru.kata.spring.boot_security.demo.DAO;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserDaoImp {

    List<User> getAllUsers();

    void saveUser(User user);

    User getUserById(Long id);

    void updateUser(User user);

    void deleteUser(Long id);

    User findByUsername(String username);
}
