package com.viniciusog.userjwtexample.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//O Spring Security providencia uma anotação chamada @AuthenticationPrincipal para
//termos acesso ao usuário logado atualmente nos Controllers
@AuthenticationPrincipal
public @interface CurrentUser {
}
