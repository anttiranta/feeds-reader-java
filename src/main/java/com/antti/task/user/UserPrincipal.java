package com.antti.task.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.antti.task.entity.Role;
import com.antti.task.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    private User user;
    
    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : this.user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Only for testing, should implement
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Only for testing, should implement
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Only for testing, should implement
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled(); 
    }
}
