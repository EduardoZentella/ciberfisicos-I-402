package com.ciberfisicos1.trazabilidad.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void whenFindByEmail_thenReturnUsuario() {
        // given
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setContraseña("password");
        usuario.setMasterKey("masterKey");
        usuario.setMasterKeyVersion("V1");
        usuario.setRole("USER");
        usuarioRepository.save(usuario);

        // when
        Usuario found = usuarioRepository.findByEmail(usuario.getEmail());

        // then
        assertThat(found.getEmail()).isEqualTo(usuario.getEmail());
        System.out.println("Prueba de UsuarioRepository exitosa: Usuario encontrado por email.");
    }
}