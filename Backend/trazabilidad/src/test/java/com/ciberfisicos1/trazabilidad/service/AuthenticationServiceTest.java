package com.ciberfisicos1.trazabilidad.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;

import com.ciberfisicos1.trazabilidad.model.authentication.AuthenticationRequest;
import com.ciberfisicos1.trazabilidad.model.authentication.AuthenticationResponse;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.historial_repository.HistorialRepository;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.authentication_service.AuthenticationService;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;

import java.util.Collections;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HistorialRepository historialRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private EncryptionService encryptionService;


    @Test
    public void whenAuthenticate_thenReturnJwt() {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("test@example.com", "password");
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setContraseña("encodedPassword");

        when(usuarioRepository.findByEmail(authRequest.getEmail())).thenReturn(usuario);
        when(passwordEncoder.matches(authRequest.getContraseña(), usuario.getContraseña())).thenReturn(true);
        when(jwtUtil.generateToken(any(String.class))).thenReturn("jwtToken");
        when(userDetailsService.loadUserByUsername(any(String.class))).thenReturn(new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.emptyList();
            }

            @Override
            public String getPassword() {
            return usuario.getContraseña();
            }

            @Override
            public String getUsername() {
            return usuario.getEmail();
            }

            @Override
            public boolean isAccountNonExpired() {
            return true;
            }

            @Override
            public boolean isAccountNonLocked() {
            return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
            return true;
            }

            @Override
            public boolean isEnabled() {
            return true;
            }
        });

        // when
        AuthenticationResponse response = authenticationService.authenticate(authRequest);

        // then
        assertThat(response.getToken()).isEqualTo("jwtToken");
        System.out.println("Prueba de AuthenticationService exitosa: JWT generado correctamente.");
    }

    @Test
    public void whenRegister_thenReturnSuccessMessage() {
        // given
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setContraseña("password");
        usuario.setMasterKey("masterKey");
        usuario.setMasterKeyVersion("V1");
        usuario.setRole("USER");

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(encryptionService.generateMasterKey()).thenReturn("generatedMasterKey");
        when(encryptionService.encryptMasterKey(any(String.class), any(String.class))).thenReturn("encryptedMasterKey");

        // when
        ResponseEntity<String> response = authenticationService.register(usuario);

        // then
        assertThat(response.getBody()).isEqualTo("Usuario registrado con éxito");
        System.out.println("Prueba de AuthenticationService exitosa: Usuario registrado con éxito.");
    }
}
