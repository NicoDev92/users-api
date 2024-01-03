package com.nicode.users.usersapi.auth.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nicode.users.usersapi.auth.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    
    
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    
    @Autowired
    public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //1. validar que sea un header autorization valido
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        //2. validar que el jwt sea valido

        String jwt = authHeader.split(" ")[1].trim();

        if(!this.jwtUtils.isValid(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        //3. cargar el usuario del UserDetailsService

        String username = this.jwtUtils.getUsername(jwt);

        User user = (User) this.userDetailsService.loadUserByUsername(username);

        //4. cargar al usuario en el contexto de seguridad

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(  user.getUsername(),
                                                                                                            user.getPassword(),
                                                                                                            user.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
