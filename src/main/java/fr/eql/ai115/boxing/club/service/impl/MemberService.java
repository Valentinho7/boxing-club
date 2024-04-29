package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.Member;
import fr.eql.ai115.boxing.club.entity.Reservation;
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

    @Autowired
    ReservationService reservationService;

    public boolean existsByEmail(String email) {
        return memberDao.existsByEmail(email);
    }

    public void save(Member member) {
        memberDao.save(member);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberDao.findMemberById(id);
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

    public Optional<Member> findByEmail(String username) {
        return memberDao.findByEmail(username);
    }

    public void validatePayment(Long id) {
        Member member = findMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.setPayementValidated(true);

        save(member);
    }

    public void validateSubscription(Long id) {
        Member member = findMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.setSubscriptionValidated(true);

        save(member);
    }

    public Member getMemberById(Long id) {
        return memberDao.findMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    public String getMemberPassword(Long memberId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return member.getPassword();
    }

    public void updateMemberPassword(Long memberId, String newPassword) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        member.setPassword(newPassword);
        memberDao.save(member);
    }

    public List<Reservation> getMemberReservations(Long memberId) {
        return reservationService.findAllByMemberId(memberId);
    }
}
