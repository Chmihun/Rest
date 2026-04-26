package ru.kata.spring.boot_security.demo.DAO;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleDao {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role findByName(String name);
}
