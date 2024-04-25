package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.Admin;
import fr.eql.ai115.boxing.club.entity.Role;
import fr.eql.ai115.boxing.club.repository.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    AdminDao adminDao;

    public void deleteAdmin(Long id) {
        adminDao.deleteById(id);
    }

    public void save(Admin admin) {
        adminDao.save(admin);

    }

    public void updateAdmin(Admin admin) {
        adminDao.save(admin);

    }

    public boolean existsByEmail(String email) {
        return adminDao.existsByEmail(email);
    }

    public Optional<Admin> findByEmail(String email) {
        return adminDao.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return new User(admin.getEmail(), admin.getPassword(), mapRolesToAuthorities(admin.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
