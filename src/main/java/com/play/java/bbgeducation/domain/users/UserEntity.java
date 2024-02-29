package com.play.java.bbgeducation.domain.users;

import com.play.java.bbgeducation.infrastructure.auth.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name="user_details")
public class UserEntity implements UserDetails {

    public UserEntity(){
        isAdmin = false;
    }

    public static UserEntity create(String firstName, String lastName, String email, String password, Boolean isAdmin){
        return UserEntity.build(null, firstName,lastName,email,password, isAdmin);
    }

    public static UserEntity build(Long id, String firstName, String lastName, String email, String password, Boolean isAdmin){
        UserEntity newUser = new UserEntity();
        newUser.setId(id);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setIsAdmin(isAdmin);
        return newUser;
    }

    @Id
    @SequenceGenerator(name = "user_details_id_seq", sequenceName = "USER_DETAILS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_details_id_seq")
    private Long id;

    @Column(unique = true)
    @Email(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,3}")
    private String email;

    private String password;
    private String firstName;
    private String lastName;

    @Column(columnDefinition ="BOOLEAN DEFAULT FALSE")
    @NotNull
    private Boolean isAdmin = false;

    //want to set as not insertable or updateable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_"+ Roles.USER));
        if (isAdmin){
            roles.add(new SimpleGrantedAuthority("ROLE_"+ Roles.ADMIN));
        }
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode(){
        return 42;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserEntity other = (UserEntity) obj;
        if (id == null) {
            return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                '}';
    }
}
