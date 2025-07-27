package com.jhampier.forohub.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

  @Autowired
  private SecurityFilter securityFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            // Public endpoints
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()
            .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()

            // Swagger/OpenAPI endpoints (if using)
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html")
            .permitAll()

            // Topico endpoints
            .requestMatchers(HttpMethod.GET, "/topicos").authenticated()
            .requestMatchers(HttpMethod.GET, "/topicos/*").authenticated()
            .requestMatchers(HttpMethod.POST, "/topicos").authenticated()
            .requestMatchers(HttpMethod.PUT, "/topicos/*").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/topicos/*").authenticated()

            // Respuesta endpoints
            .requestMatchers(HttpMethod.GET, "/respuestas").authenticated()
            .requestMatchers(HttpMethod.GET, "/respuestas/*").authenticated()
            .requestMatchers(HttpMethod.POST, "/respuestas").authenticated()
            .requestMatchers(HttpMethod.PUT, "/respuestas/*").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/respuestas/*").authenticated()

            // Usuario endpoints (admin only for some operations)
            .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/usuarios/*").authenticated()
            .requestMatchers(HttpMethod.PUT, "/usuarios/*").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/usuarios/*").hasRole("ADMIN")

            // Curso endpoints
            .requestMatchers(HttpMethod.GET, "/cursos").authenticated()
            .requestMatchers(HttpMethod.GET, "/cursos/*").authenticated()
            .requestMatchers(HttpMethod.POST, "/cursos").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/cursos/*").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/cursos/*").hasRole("ADMIN")

            // Perfil endpoints (admin only)
            .requestMatchers("/perfiles/**").hasRole("ADMIN")

            // All other requests require authentication
            .anyRequest().authenticated())
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}