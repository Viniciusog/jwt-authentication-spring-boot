package com.viniciusog.userjwtexample.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viniciusog.userjwtexample.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
//'SER PRINCIPAL' É UM 'USER DETAILS'
public class UserPrincipal implements UserDetails {
    //DEFINIÇÃO
    //O Spring utilizará os dados armazenados em objeto dessa classe para fazer AUTENTICAÇÕES e AUTORIZAÇÕES
    //Esta é a classe cuja instâncias serão retornadas em CustomUserDetailsService
    private Long id;
    private String username;
    private String name;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String password;

    //'Permissões' que o nosso usuário possui
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String username, String name, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    //Estamos recebendo um usuário, pegando todas as permissões dele e adicionando em uma lista
    //Em seguida, retornando um OBJETO do tipo UserPrincipal
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

        //Retornando um objeto UserPrincipal com os dados de usuário e as permissões
        return new UserPrincipal (
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
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
}
