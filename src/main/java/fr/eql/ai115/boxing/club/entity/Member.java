package fr.eql.ai115.boxing.club.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "member")
public class Member implements UserDetails {

    /// Attributs ///

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String email;
    private String phoneNumber;
    private String address;

    @JsonIgnore
    private String password;

    private LocalDate registrationDate;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Reservation> reservations = new ArrayList<>();

    @ManyToMany(fetch=FetchType.EAGER)
    private List<Role> roles;

    /// Getters ///

    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public LocalDate getBirthdate() {
        return birthdate;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAddress() {
        return address;
    }

    @JsonIgnore
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    public List<Reservation> getReservations() {
        return reservations;
    }
    public List<Role> getRoles() {
        return roles;
    }

    /// Setters ///

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /// ToString ///
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthdate=" + birthdate +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(firstname, member.firstname) && Objects.equals(lastname, member.lastname) && Objects.equals(birthdate, member.birthdate) && Objects.equals(email, member.email) && Objects.equals(phoneNumber, member.phoneNumber) && Objects.equals(address, member.address) && Objects.equals(password, member.password) && Objects.equals(registrationDate, member.registrationDate) && Objects.equals(reservations, member.reservations) && Objects.equals(roles, member.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, birthdate, email, phoneNumber, address, password, registrationDate, reservations, roles);
    }

    /// Methods UserDetails ///

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
    @Override
    public boolean isEnabled() {
        return false;
    }


}
