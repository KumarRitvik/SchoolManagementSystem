package com.ritvik.school.user_service.security;

import com.ritvik.school.user_service.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AppUserDetails implements UserDetails {

    private final User dbuser;

    public AppUserDetails(User dbuser) {
        this.dbuser = dbuser;
    }


    //Spring Security AppUserDetails ke Object ko samajhta hai
    // islye ham apne db ke user ko AppUserDetails ke object me wrap karte hai,
    // taki Spring Security usko samajh sake. AppUserDetails me wahi fields hote hai
    //jo Hamare UserDetails me hote hai.. (lets say alag alag jagah fiels like username,
    // role, password, etc ke naam alag alag ho sakte hai isliye spring me standard fields
    // ke nam set hai in the AppUserDetails jisse spring ko confusion nahi hooga,
    // ap apne man ke field names lo aur use bas AppUserDetails me wrap kar do spring un
    // fields ko samajh jayega)

    //for wrapping field username
    @Override
    public String getUsername() {return dbuser.getUsername();
    }
    //for wrapping field password
    @Override
    public String getPassword() {
        return dbuser.getPassword();
    }
    //for wrapping field Role
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + dbuser.getRole().getRoleName()));
    }

    //for wrapping field isCredentialsNonExpired
    @Override
    public boolean isCredentialsNonExpired() {
        return dbuser.isCredentialsNonExpired();
    }

    //for wrapping field isAccountNonExpired
    @Override
    public boolean isAccountNonExpired() {
        return dbuser.isAccountNonExpired();
    }


    //for wrapping field isEnabled
    @Override
    public boolean isEnabled() {
        return dbuser.isEnabled();
    }
    //for wrapping field isAccountNonLocked
    @Override
    public boolean isAccountNonLocked() {
        return dbuser.isAccountNonLocked();
    }



}