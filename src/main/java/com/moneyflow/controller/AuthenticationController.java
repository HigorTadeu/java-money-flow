package com.moneyflow.controller;

import com.moneyflow.dto.AuthenticationDTO;
import com.moneyflow.dto.LoginResponseDTO;
import com.moneyflow.dto.LogoutRequestDTO;
import com.moneyflow.dto.RegisterRequestDTO;
import com.moneyflow.entity.User;
import com.moneyflow.repository.UserRepository;
import com.moneyflow.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository repository;

    public AuthenticationController(UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService){
        this.repository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User)auth.getPrincipal());//Gera o token do usuario autenticado e retorna

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequestDTO dto){
        var result = tokenService.revokedToken(dto.token());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestDTO data){
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.name(), data.email(), data.login(), encryptedPassword, data.role(), data.status());
        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
