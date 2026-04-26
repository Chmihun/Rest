package ru.kata.spring.boot_security.demo.DAO;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDao implements UserDaoImp {
    @PersistenceContext
    private EntityManager entityManager;

    public UserDao(EntityManager entityManager) {this.entityManager = entityManager;}

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();

    }

    @Override
    public void saveUser(User user) {
        //        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.roles" +
                " WHERE u.id = :id", User.class).setParameter("id", id).getSingleResult();

    }

    @Override
    public void updateUser(User user) {
        User existing = getUserById(user.getId());
        existing.setName(user.getName());
        existing.setSurname(user.getSurname());
        existing.setAge(user.getAge());

        entityManager.merge(existing);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        entityManager.remove(user);
    }

    @Override
    public User findByUsername(String username) {
        return entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.name = :username", User.class).setParameter("username", username).getSingleResult();

    }
}
