package org.global.console.service;

import org.global.console.dto.request.create.CreateUserDto;
import org.global.console.dto.request.update.UpdateUserDto;
import org.global.console.model.Usuario;
import org.global.console.repository.UsuarioRepository;
import org.global.console.services.UsuarioService;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsuarioServiceTest {


    private UsuarioService usuarioService;

    @BeforeAll
    void setUp() {
        usuarioService = UsuarioService.getInstance();
        usuarioService.getUsuarioRepository().truncate();
    }

    @AfterAll
    void tearDown() {
        usuarioService.getUsuarioRepository().truncate();
    }

    @Test
    void testFindById()  {

        Assertions.assertDoesNotThrow(() -> {
            CreateUserDto createUserDto = new CreateUserDto("user.test", "User Test", "user.test@example.com", "123456");
            Usuario usuario = usuarioService.createUser(createUserDto);

            assertNotNull(usuario);

            Usuario foundUsuario = usuarioService.getUserByLogin("user.test");

            assertNotNull(foundUsuario);
            assertEquals(usuario.getLogin(), foundUsuario.getLogin());
            assertEquals(usuario.getNome(), foundUsuario.getNome());
            assertEquals(usuario.getEmail(), foundUsuario.getEmail());
            assertEquals(usuario.getSenha(), foundUsuario.getSenha());
        });
    }

    @Test
    void testSaveUsuario() {

        Assertions.assertDoesNotThrow(() -> {
            CreateUserDto createUserDto = new CreateUserDto("user.test2", "User Test", "user.test2@example.com", "123456");
            Usuario usuario = usuarioService.createUser(createUserDto);

            assertNotNull(usuario);
            assertEquals(createUserDto.login(), usuario.getLogin());
            assertEquals(createUserDto.nome(), usuario.getNome());
            assertEquals(createUserDto.email(), usuario.getEmail());
            assertEquals(createUserDto.senha(), usuario.getSenha());
        });
    }

    @Test
    void testDeleteUsuario() {

        Assertions.assertDoesNotThrow(() -> {
            CreateUserDto createUserDto = new CreateUserDto("user.test3", "User Test", "user.test3@example.com", "123456");
            Usuario usuario = usuarioService.createUser(createUserDto);

            assertNotNull(usuario);

            boolean result = usuarioService.deleteUser(usuario.getLogin());

            assertTrue(result);
        });
    }

    @Test
    void testUpdateUsuario() {

        Assertions.assertDoesNotThrow(() -> {
            CreateUserDto createUserDto = new CreateUserDto("user.test4", "User Test", "user.test4@example.com", "123456");

            Usuario usuario = usuarioService.createUser(createUserDto);

            assertNotNull(usuario);

            UpdateUserDto updateUserDto = new UpdateUserDto("user.test4", "User Test Updated", "user.test4@updated.com", "newpassword");

            Usuario updatedUsuario = usuarioService.updateUser(updateUserDto);

            assertNotNull(updatedUsuario);
            assertEquals(updateUserDto.login(), updatedUsuario.getLogin());
            assertEquals(updateUserDto.nome(), updatedUsuario.getNome());
            assertEquals(updateUserDto.email(), updatedUsuario.getEmail());
            assertEquals(updateUserDto.senha(), updatedUsuario.getSenha());
        });
    }

    @Test
    void testGetAllUsuarios() {

        Assertions.assertDoesNotThrow(() -> {

            usuarioService.getUsuarioRepository().truncate();

            CreateUserDto createUserDto = new CreateUserDto("user.test5", "User Test", "user.test5@example.com", "123456");
            Usuario usuario = usuarioService.createUser(createUserDto);
            assertNotNull(usuario);

            CreateUserDto createUserDto2 = new CreateUserDto("user.test6", "User Test", "user.test@example.com", "123456");
            Usuario usuario2 = usuarioService.createUser(createUserDto2);
            assertNotNull(usuario2);

            List<Usuario> usuarios = usuarioService.getAllUsers();

            assertNotNull(usuarios);
            assertFalse(usuarios.isEmpty());
            assertEquals(2, usuarios.size());
        });
    }
}