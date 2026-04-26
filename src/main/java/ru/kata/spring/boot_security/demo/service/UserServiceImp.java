package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DAO.RoleDao;
import ru.kata.spring.boot_security.demo.DAO.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

//    @PersistenceContext
    private final RoleService roleService;  // добавить
    private final PasswordEncoder passwordEncoder;
    private final RoleDao roleDao;
    private final UserDao userDao;

    public UserServiceImp(RoleService roleService, PasswordEncoder passwordEncoder, RoleDao roleDao, UserDao userDao) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
        this.userDao = userDao;
    }
    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
    @Transactional
    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
    }
    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }
    @Transactional
    @Override
    public void updateUser(User user, List<Long> roleIds) {
        // Получаем роли по ID
        Set<Role> roles = roleIds.stream()
                .map(roleService::getRoleById)
                .collect(Collectors.toSet());
        user.setRoles(roles);

        // Если пароль изменился - шифруем
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userDao.updateUser(user);
    }
    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userDao.deleteUser(id);
    }
    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        try {
            return findByUsername(name);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User " + name + " not found");
        }
    }
}
