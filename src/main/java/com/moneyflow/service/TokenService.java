package com.moneyflow.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.moneyflow.entity.RevokedToken;
import com.moneyflow.entity.User;
import com.moneyflow.repository.RevokedTokenRepository;
import com.moneyflow.service.exception.JWTCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    private final String applicationName = "moneyFlow-api";

    @Autowired
    RevokedTokenRepository revokedTokenRepository;

    /**
     * Método responsável por gerar o Token
     * @param user Usuário que está gerando o token
     * @return Retorna a String com o token gerado
     */
    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer(applicationName) //adiciona quem está criando o token
                    .withSubject(user.getLogin()) //adiciona o usuário no token
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generation token");
        }
    }

    /**
     * Méotodo para verificar se o Token enviado é válido
     * @param token
     * @return
     *      Caso seja válido retorna a String do Usuário que existe no Token
     *      Caso NÃO seja válido retorna uma String vazia
     */
    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(applicationName)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException exception) {
            return "";
        }
    }

    public String revokedToken(String token){
        if(validateToken(token) != ""){
            revokedTokenRepository.save(new RevokedToken(token, LocalDateTime.now()));
            SecurityContextHolder.clearContext(); //Limpa o contexto de segurança
            return "Logout successfully";
        }
        return "Invalid token";
    }

    /**
     * Método que retorna o tempo (Instant) que o Token deve expirar com base no fuso horário de brasília
     * Tempo de expiração está como 01 hora
     * @return Instant
     */
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }
}
