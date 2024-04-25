package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.Member;
import fr.eql.ai115.boxing.club.entity.Role;
import fr.eql.ai115.boxing.club.repository.MemberDao;
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
public class MemberService implements UserDetailsService{

    @Autowired
    MemberDao memberDao;

    public boolean existsByEmail(String email) {
        return memberDao.existsByEmail(email);
    }

    public void save(Member member) {
        memberDao.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return new User(member.getEmail(), member.getPassword(), mapRolesToAuthorities(member.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
