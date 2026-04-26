package ru.kata.spring.boot_security.demo.DAO;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImp implements RoleDao {
    @PersistenceContext
    private final EntityManager entityManager;


    public RoleDaoImp(EntityManager entityManager) {this.entityManager = entityManager;}


    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();

    }

    @Override
    public Role getRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }


    @Override
    public Role findByName(String name) {
        try {
            return entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class).setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
