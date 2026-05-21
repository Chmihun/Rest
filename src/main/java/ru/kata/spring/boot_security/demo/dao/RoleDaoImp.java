package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleDaoImp implements RoleDao {
    @PersistenceContext
    private final EntityManager entityManager;


    public RoleDaoImp(EntityManager entityManager) {this.entityManager = entityManager;}


    @Override
    public Set<Role> getAllRoles() {
        List<Role> roles = entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
        return new LinkedHashSet<>(roles);
    }

}
