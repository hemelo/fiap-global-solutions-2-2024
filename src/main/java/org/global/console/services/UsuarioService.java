package org.global.console.services;

import org.global.console.dto.request.CreateUserDto;
import org.global.console.dto.request.UpdateUserDto;
import org.global.console.model.Usuario;
import org.global.console.repository.UsuarioRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    // Constructor accepts the connection to the repository
    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository(connection);
    }

    public Usuario createUser(CreateUserDto createUserDto) throws SQLException {
        Usuario usuario = Usuario.builder()
                .nome(createUserDto.nome())
                .email(createUserDto.email())
                .senha(createUserDto.senha())
                .permissao(createUserDto.permissao())
                .build();

        usuarioRepository.save(usuario);
        return usuario;
    }

    public Usuario updateUser(UpdateUserDto updateUserDto) throws SQLException {
        Usuario usuario = Usuario.builder()
                .id(updateUserDto.id())
                .nome(updateUserDto.nome())
                .email(updateUserDto.email())
                .senha(updateUserDto.senha())
                .permissao(updateUserDto.permissao())
                .build();

        usuarioRepository.update(usuario);
        return usuario;
    }

    public List<Usuario> getAllUsers() throws SQLException {
        return usuarioRepository.findAll();
    }

    public Usuario getUserById(Long id) throws SQLException {
        return usuarioRepository.findById(id);
    }

    public void deleteUser(Long id) throws SQLException {
        usuarioRepository.delete(id);
    }
}
