package com.viniciusog.userjwtexample.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    //Retornará um objeto do tipo UserDetails, como UserPrincipal
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
