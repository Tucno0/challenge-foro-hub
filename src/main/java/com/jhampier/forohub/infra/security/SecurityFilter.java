package com.jhampier.forohub.infra.security;

import com.jhampier.forohub.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

  @Autowired
  private TokenService tokenService;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // Get token from Authorization header
    var authHeader = request.getHeader("Authorization");

      System.out.println("authHeader = " + authHeader);

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      var token = authHeader.replace("Bearer ", "");
      var subject = tokenService.getSubject(token);
        System.out.println("subject = " + subject);

      if (subject != null) {
        // Token is valid, authenticate user
        var usuarioOptional = usuarioRepository.findByCorreoElectronico(subject);
        if (usuarioOptional.isPresent()) {
          var usuario = usuarioOptional.get();
          var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}