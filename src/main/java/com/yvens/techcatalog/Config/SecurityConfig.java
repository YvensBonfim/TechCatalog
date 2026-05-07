package com.yvens.techcatalog.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    @Order(3)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        // Desativa CSRF (comum para APIs Stateless/JWT)
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
            // Resolve o aviso de deprecation: use apenas strings para o H2
            .requestMatchers("/h2-console/**").permitAll()
            
            // Libera Categorias e Produtos para que o @PreAuthorize no Resource decida quem entra
            .requestMatchers("/categories/**").permitAll()
            .requestMatchers("/products/**").permitAll()
            
            // Qualquer outra rota (ex: /users/**) exige token válido (401 se não houver)
            .anyRequest().authenticated()
        );

        // Configura o Resource Server para processar o JWT
        http.oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );

        // Ajuste para frames do H2 Console
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        
        // Lê as permissões do campo "authorities" do seu JWT
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        
        // Remove prefixos automáticos para bater com o seu seed (ROLE_ADMIN)
        grantedAuthoritiesConverter.setAuthorityPrefix(""); 

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}