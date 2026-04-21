package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImp implements UserService, UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;
    private final RoleService roleService;  // добавить
    private final PasswordEncoder passwordEncoder;


    public UserServiceImp(RoleService roleService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :id", User.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public void updateUser(User user, List<Long> roleIds) {
        System.out.println("Updating password: " + (user.getPassword() != null));
        User existing = getUserById(user.getId());
        existing.setName(user.getName());
        existing.setSurname(user.getSurname());
        existing.setAge(user.getAge());

        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (roleIds != null && !roleIds.isEmpty()) {
            existing.setRoles(roleIds.stream().map(roleService::getRoleById).collect(Collectors.toSet()));
        }
        entityManager.merge(existing);

    }

    /*
    public void updateUser(User user) {
        User existingUser = getUserById(user.getId());
        // Обновляем основные поля
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setAge(user.getAge());
        // Обновляем пароль ТОЛЬКО если он не пустой

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        // Если пароль пустой - оставляем старый (ничего не делаем)

        // Обновляем роли ТОЛЬКО если они переданы
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            existingUser.setRoles(user.getRoles());
        }
        // Если роли не переданы - оставляем старые
        entityManager.merge(existingUser);
    }

*/
    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        entityManager.remove(user);
    }

    @Override
    public User findByUsername(String username) {
        return entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.name = :username", User.class).setParameter("username", username).getSingleResult();
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
