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

        
        
     
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
          
            .requestMatchers("/h2-console/**").permitAll()
            
          
            .requestMatchers("/categories/**").permitAll()
            .requestMatchers("/products/**").permitAll()
            .requestMatchers("/auth/recover-token", "/auth/**").permitAll()
            .requestMatchers("/auth/recover/**").permitAll()
            
          
            .anyRequest().authenticated()
        );

      
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
        
       
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        
      
        grantedAuthoritiesConverter.setAuthorityPrefix(""); 

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}