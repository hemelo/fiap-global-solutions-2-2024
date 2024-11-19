package org.global.console.services;

import lombok.Getter;
import org.global.console.dto.Sessao;
import org.global.console.dto.request.LoginDto;
import org.global.console.dto.request.create.CreateUserDto;
import org.global.console.dto.request.update.UpdateUserDto;
import org.global.console.dto.response.UsuarioResponse;
import org.global.console.model.Usuario;
import org.global.console.repository.LogRepository;
import org.global.console.repository.UsuarioRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Serviço para manipulação de usuários.
 * Contém métodos para criação, atualização, listagem e remoção de usuários.
 * É um singleton, ou seja, só pode existir uma instância dessa classe por vez.
 */
public class UsuarioService {
    private static UsuarioService instance;

    @Getter
    private final UsuarioRepository usuarioRepository;

    private final LogRepository logRepository;

    private UsuarioService() {
        this.usuarioRepository = UsuarioRepository.getInstance();
        this.logRepository = LogRepository.getInstance();
    }

    public static synchronized UsuarioService getInstance() {
        if (instance == null) {
            instance = new UsuarioService();
        }

        return instance;
    }

    public Usuario createUser(CreateUserDto createUserDto) throws SQLException {
        Usuario usuario = Usuario.builder()
                .login(createUserDto.login())
                .nome(createUserDto.nome())
                .email(createUserDto.email())
                .senha(createUserDto.senha())
                .build();

        usuarioRepository.save(usuario);
        return usuario;
    }

    public Usuario updateUser(UpdateUserDto updateUserDto) throws SQLException {
        Usuario usuario = Usuario.builder()
                .login(updateUserDto.login())
                .nome(updateUserDto.nome())
                .email(updateUserDto.email())
                .senha(updateUserDto.senha())
                .build();

        usuarioRepository.update(usuario);
        return usuario;
    }

    public List<Usuario> getAllUsers() throws SQLException {
        return usuarioRepository.findAll();
    }

    public Usuario getUserByLogin(String login) throws SQLException {
        return usuarioRepository.findByLogin(login);
    }

    public boolean deleteUser(String login) throws SQLException {
        return usuarioRepository.delete(login);
    }

    public Sessao login(LoginDto loginDto) throws SQLException {
        Usuario usuario = usuarioRepository.findByLogin(loginDto.username());

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        if (!usuario.getSenha().equals(loginDto.senha())) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        return new Sessao(usuario.getLogin(), usuario.getNome(), usuario.getEmail());
    }

    public UsuarioResponse viewUsuario(String login) throws SQLException {
        Usuario usuario = usuarioRepository.findByLogin(login);
        return toUsuarioResponse(usuario);
    }

    public List<UsuarioResponse> viewAllUsuarios() throws SQLException {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return Objects.requireNonNullElse(usuarios, new ArrayList<Usuario>()).stream()
                .map(this::toUsuarioResponse)
                .collect(Collectors.toList());
    }

    private UsuarioResponse toUsuarioResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getLogin(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt()
        );
    }
}
