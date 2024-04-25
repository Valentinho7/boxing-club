package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.Admin;
import fr.eql.ai115.boxing.club.entity.Member;
import fr.eql.ai115.boxing.club.entity.Role;
import fr.eql.ai115.boxing.club.repository.AdminDao;
import fr.eql.ai115.boxing.club.repository.MemberDao;
import fr.eql.ai115.boxing.club.repository.RoleDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    AdminService adminService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberDao memberDao;

    @Autowired
    AdminDao adminDao;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberDao.findByEmail(username);
        Optional<Admin> optionalAdmin = adminDao.findByEmail(username);
        if (optionalMember.isPresent()) {
            if (!memberDao.hasRole(username, "MEMBER")) {
                throw new UsernameNotFoundException("Invalid username: " + username);
            } else {
                return memberService.loadUserByUsername(username);
            }
        } else if (optionalAdmin.isPresent()) {
            if (!adminDao.hasRole(username, "ADMIN")) {
                throw new UsernameNotFoundException("Invalid username: " + username);
            } else {
                return adminService.loadUserByUsername(username);
            }
        } else {
            throw new UsernameNotFoundException("Invalid username: " + username);
        }
    }
}