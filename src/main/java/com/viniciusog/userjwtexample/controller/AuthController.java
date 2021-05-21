package com.viniciusog.userjwtexample.controller;

import com.viniciusog.userjwtexample.exceptions.AppException;
import com.viniciusog.userjwtexample.model.Role;
import com.viniciusog.userjwtexample.model.User;
import com.viniciusog.userjwtexample.model.RoleName;
import com.viniciusog.userjwtexample.payload.ApiResponse;
import com.viniciusog.userjwtexample.payload.JwtAuthenticationResponse;
import com.viniciusog.userjwtexample.payload.LoginRequest;
import com.viniciusog.userjwtexample.payload.SignUpRequest;
import com.viniciusog.userjwtexample.repositories.RoleRepository;
import com.viniciusog.userjwtexample.repositories.UserRepository;
import com.viniciusog.userjwtexample.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        //Estamos criando uma autenticação com username/email e senha do usuário
        //O Spring Security está pegando automaticamente os dados do usuário através do loadUserByUsername()
        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken (
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        //Passa a autenticação criada com username/email e senha do usuário para o contexto do SpringSecurity
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Cria token JWT
        String jwt = jwtTokenProvider.generateToken(authentication);

        //Retorna JwtAuthenticationResponse com accessToken=jwt e tokenType="Bearer"
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser (@Valid @RequestBody SignUpRequest signUpRequest) {

        try {
            //Verifica se já existe usuário por username
            if(userRepository.existsByUsername(signUpRequest.getUsername())) {
                return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                        HttpStatus.BAD_REQUEST);
            }

            //Verifica se já existe usuário por email
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return new ResponseEntity(new ApiResponse(false, "E-mail Address already in use!"),
                        HttpStatus.BAD_REQUEST);
            }

            //Criando conta do usuário
            User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                    signUpRequest.getEmail(), signUpRequest.getPassword());

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));

            user.setRoles(Collections.singleton(userRole));

            User result = userRepository.save(user);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/api/users/{username}")
                    .buildAndExpand(result.getUsername()).toUri();

            return ResponseEntity.created(location).body(new ApiResponse(true,
                    "User registered successfully"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
