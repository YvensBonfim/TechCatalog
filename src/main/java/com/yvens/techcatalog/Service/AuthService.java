package com.yvens.techcatalog.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yvens.techcatalog.DTO.EmailDto;
import com.yvens.techcatalog.DTO.NewPasswordDto;
import com.yvens.techcatalog.Entity.PasswordRecover;
import com.yvens.techcatalog.Entity.User;
import com.yvens.techcatalog.Repository.PasswordRecoverRepository;
import com.yvens.techcatalog.Repository.UserRepository;
import com.yvens.techcatalog.Service.Exception.ResourceNotFoundException;

import jakarta.validation.Valid;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;


    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecover;

    @Autowired
    private EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Transactional
    public void createRecoveryToken(EmailDto body) {

        String token =UUID.randomUUID().toString();

        User user = userRepository.findByEmail(body.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("Email n~ao encontrado");

        }

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
       entity=passwordRecover.save(entity);

       String text ="Acesse o link para redifinir uma nova senha\n\n"+ recoverUri + token + "validade de "+ tokenMinutes+ " minutos";
      emailService.sendEmail(entity.getEmail(), "recuperacao de senha", text);

    }


@Transactional
public void saveNewPassword(@Valid NewPasswordDto body) { 

   
    List<PasswordRecover> result = passwordRecover.searchValidTokens(body.getToken(), Instant.now());
    
  
    if (result.isEmpty()) {
        throw new ResourceNotFoundException("Token inválido ou expirado");
    }
    
   
    User user = userRepository.findByEmail(result.get(0).getEmail());
    
    
    user.setPassword(passwordEncoder.encode(body.getPassword())); 
    userRepository.save(user);
}

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return userRepository.findByEmail(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

}
