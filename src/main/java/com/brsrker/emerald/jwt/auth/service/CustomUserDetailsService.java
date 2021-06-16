package com.brsrker.emerald.jwt.auth.service;

import com.brsrker.emerald.jwt.auth.entity.Privilege;
import com.brsrker.emerald.jwt.auth.entity.Role;
import com.brsrker.emerald.jwt.auth.entity.User;
import com.brsrker.emerald.jwt.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        final User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + loginId));

        boolean expired = user.getExpire() && LocalDateTime.now().compareTo(user.getExpireAt()) < 0;


        return new org.springframework.security.core.userdetails.User(
                user.getLoginId(), user.getPassword(), user.getEnabled(), !expired, true,
                !user.getLocked(), getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Set<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        return roles.stream()
                .map(Role::getPrivileges)
                .flatMap(privileges -> privileges.stream()
                        .map(Privilege::getName))
                .collect(Collectors.toList());
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        return grantedAuthorities;
    }

}