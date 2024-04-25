package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.Role;
import fr.eql.ai115.boxing.club.repository.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

        @Autowired
        RoleDao roleRepository;

        public Role findByName(String name) {
            return roleRepository.findByName(name)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + name));
        }
}
