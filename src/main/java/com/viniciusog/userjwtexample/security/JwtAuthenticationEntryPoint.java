package com.viniciusog.userjwtexample.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //DEFINIÇÃO
    //Esta classe serve para realizar uma determinada função todas as vezes em que
    //o usuário autenticado tenha tentado acessar algo que ele não pode

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    //Este método é chamado todas as vezes em que for gerado uma exceção devido um acesso não autorizado
    //feito por um usuário
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
}
