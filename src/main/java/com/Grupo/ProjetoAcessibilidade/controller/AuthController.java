package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.AuthRequestDTO;
import com.Grupo.ProjetoAcessibilidade.dto.AuthResponseDTO;
import com.Grupo.ProjetoAcessibilidade.dto.UsuarioDTO;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.security.JwtUtil;
import com.Grupo.ProjetoAcessibilidade.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );


            String token = jwtUtil.generateToken(authentication.getName());

            return ResponseEntity.ok(new AuthResponseDTO(token, "Login realizado com sucesso"));

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponseDTO(null, "Credenciais inválidas"));
        }
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody UsuarioDTO usuarioDTO) {
        try {

            Usuario usuarioRegistrado = usuarioService.salvar(usuarioDTO);


            String token = jwtUtil.generateToken(usuarioRegistrado.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponseDTO(token, "Usuário registrado com sucesso"));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponseDTO(null, "Erro no registro: " + ex.getMessage()));
        }
    }
}
