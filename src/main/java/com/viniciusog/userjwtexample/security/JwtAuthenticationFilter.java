package com.viniciusog.userjwtexample.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //DEFINIÇÃO DA CLASSE
    //PARA CADA REQUEST, A NOSSA CLASSE PEGARÁ O Token Jwt, enviará oara JwtTokenProvider,
    //esse, validará o token e verificará se é correspondente ao token do usuário logado.

    //JWTAuthenticationFilter: get the JWT token from the request, validate it,
    // load the user associated with the token, and pass it to Spring Security -

    //The JwtAuthenticationFilter will read the accessToken from the header, verify it,
    // and allow/deny access to the API.

    //Providencia um token
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //Retorna UserPrincipal em loadUserbyUsername()
    @Autowired
    private  CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String jwt = getJwtFromTheRequest(httpServletRequest);

            //TODO:
            // Ao fazer login, é retornado um token jwt que possui, dentro dele, o id do usuário
            // Ao realizar uma requisição, a nossa classe receberá o token, pegará o id do usuário dentro dele
            // e verificará se condiz com o id do usuário logado atualmente no contexto Authentication do Spring Security
            // SE o id for condizente, então pegaremos todos os dados do usuário no banco de dados
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                //Pegamos o id do usuário pelo JWT passado pelo Header da request
                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);

                //Pegamos os dados do usuário do banco de dados, pelo id
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);

                //Estamos configurando a autenticação do usuário dentro do contexto do Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromTheRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
