package com.example.springboot.service;

import com.example.springboot.model.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getAllRoles();
    Role getRoleByID(Long id);
    Role getRoleByName(String name);
    void addRole(Role role);
}

