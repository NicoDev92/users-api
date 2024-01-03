package com.nicode.users.usersapi.controllers;

import com.nicode.users.usersapi.DTO.LoginDto;
import com.nicode.users.usersapi.auth.JwtUtils;
import com.nicode.users.usersapi.services.UserSecurityService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usersApi/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserSecurityService userSecurityService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
            UserSecurityService userSecurityService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userSecurityService = userSecurityService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto) {

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        } catch (AuthenticationException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuario o contraseña incorrectos");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String[] roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);

        String jwt = jwtUtils.create(loginDto.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("user", userDetails);
        response.put("role", roles);

        return ResponseEntity.ok().body(response);
    }

}
