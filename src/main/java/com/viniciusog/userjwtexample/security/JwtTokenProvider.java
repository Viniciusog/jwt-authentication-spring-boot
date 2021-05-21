package com.viniciusog.userjwtexample.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtTokenProvider {
    //CLASSE RESPONSÁVEL PELA:
    // - CRIAÇÃO DE UM JWT QUANDO UM USUÁRIO LOGA COM SUCESSO
    // - VALIDAÇÃO DO JWT RECEBIDO


    private static final Logger logger =  LoggerFactory.getLogger(JwtTokenProvider.class);

    //ESTÁ ESCRITO EM application.properties
    //Serve como uma chave para proteger o token e fazer verificações no mesmo
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    //ESTÁ ESCRITO EM application.properties
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    //Gera um token apartir da autenticação atual
    //DA ONDE VEM ESSA AUTENTICAÇÃO?
    public String generateToken(Authentication authentication){

        //Provavelmente o SpringSecurity está carregando os dados de usuário apartir do loadUserByUsename()
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    //Estamos pegando o token e depois, o id do usuário relacionado a esse token
    //Conseguimos pegar o id do usuário através do token porque inserimos ele na hora da criação do token
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    //Verificar se o token informado pelo parâmetro é o correto para o usuário da autenticação atual
    public boolean validateToken(String authToken) {
        try {
            //Estamos pegando o token pelo jwtSecret e verificando se o token encontrado está de acordo
            //com o token passado pelo parâmetro
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
