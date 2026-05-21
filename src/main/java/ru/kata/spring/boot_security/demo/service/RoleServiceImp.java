package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImp implements RoleService {
    @PersistenceContext
    private EntityManager entityManager;
    private final RoleDao roleDao;

    public RoleServiceImp(RoleDao roleDao) {this.roleDao = roleDao;}


    @Transactional(readOnly = true)
    @Override
    public List<Role> getAllRoles() {
        return new ArrayList<>(roleDao.getAllRoles());
    }

    @Transactional(readOnly = true)
    @Override
    public Role getRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }

}