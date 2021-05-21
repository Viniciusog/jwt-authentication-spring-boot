package com.viniciusog.userjwtexample.security;

import com.viniciusog.userjwtexample.exceptions.ResourceNotFoundException;
import com.viniciusog.userjwtexample.model.User;
import com.viniciusog.userjwtexample.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //MÉTODO USADO PELO Spring security
    //Para autenticar um usuário, o spring security precisa de uma forma para carregar os dados dos usuários
    //Assim, esta é a função do método abaixo
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        //Procuramos o usuário no banco de dados
        //Proporciona login via email ou username
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email or username."));

        //Retornamos os dados do usuário USER PRINCIPAL, junto com as permissões GrantedAuthorities
        return UserPrincipal.create(user);
    }

    //MÉTODO USADO PELO JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return UserPrincipal.create(user);
    }
}
