package com.ciberfisicos1.trazabilidad.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ciberfisicos1.trazabilidad.model.authentication.AuthenticationRequest;
import com.ciberfisicos1.trazabilidad.model.authentication.AuthenticationResponse;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.authentication_service.AuthenticationService;
import com.ciberfisicos1.trazabilidad.service.usuario_details_service.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private UsuarioDetailsService usuarioDetailsService;

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    public void whenAuthenticate_thenReturnJwt() throws Exception {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("test@example.com", "password");
        AuthenticationResponse authResponse = new AuthenticationResponse("jwtToken");

        Mockito.when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class)))
               .thenReturn(authResponse);

        // when
        mockMvc.perform(post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwtToken"));

        System.out.println("Prueba de AuthController exitosa: JWT devuelto correctamente.");
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void whenRegister_thenReturnSuccessMessage() throws Exception {
        // given
        Usuario usuario = new Usuario();
        usuario.setEmail("newuser@example.com");
        usuario.setContraseña("password");
        usuario.setMasterKey("masterKey");
        usuario.setMasterKeyVersion("V1");
        usuario.setRole("USER");

        String jwt = "Bearer jwtToken";
        String email = "admin@example.com";
        Usuario adminUser = new Usuario();
        adminUser.setEmail(email);
        adminUser.setRole("ADMIN");

        Mockito.when(jwtUtil.extractEmail(Mockito.anyString())).thenReturn(email);
        Mockito.when(jwtUtil.validateToken(Mockito.anyString(), Mockito.any())).thenReturn(true);
        Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(adminUser);
        Mockito.when(authenticationService.register(Mockito.any(Usuario.class)))
               .thenReturn(ResponseEntity.ok("Usuario registrado con éxito"));

        // when
        mockMvc.perform(post("/api/auth/register")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(usuario)))
                .andExpect(status().isOk());

        System.out.println("Prueba de AuthController exitosa: Usuario registrado con éxito.");
    }
}
