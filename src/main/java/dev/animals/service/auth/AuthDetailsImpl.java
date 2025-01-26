package dev.animals.service.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.animals.entity.AuthEntity;
import dev.animals.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AuthDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String username;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

  public AuthDetailsImpl(String username, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

  public static AuthDetailsImpl build(AuthEntity auth) {
        Role role = auth.getRole();
        if (role == null) {
            role = Role.USER;
        }
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.name()));
        return new AuthDetailsImpl(
                auth.getUsername(),
                auth.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
      result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AuthDetailsImpl other = (AuthDetailsImpl) obj;
      if (username == null) {
        if (other.username != null)
                return false;
      } else if (!username.equals(other.username))
            return false;
        return true;
    }

}