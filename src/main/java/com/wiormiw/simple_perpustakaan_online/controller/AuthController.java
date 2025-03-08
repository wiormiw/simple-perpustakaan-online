package com.wiormiw.simple_perpustakaan_online.controller;

import com.wiormiw.simple_perpustakaan_online.config.jwt.JwtUtil;
import com.wiormiw.simple_perpustakaan_online.models.CustomUserDetails;
import com.wiormiw.simple_perpustakaan_online.models.dto.auth.AuthRequestDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.auth.AuthResponseDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserCreateDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserDTO;
import com.wiormiw.simple_perpustakaan_online.service.implementation.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserServiceImpl userServiceImpl;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService,
            UserServiceImpl userServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(dto.email());

        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }


    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        return ResponseEntity.ok(userServiceImpl.createUser(dto));
    }
}
