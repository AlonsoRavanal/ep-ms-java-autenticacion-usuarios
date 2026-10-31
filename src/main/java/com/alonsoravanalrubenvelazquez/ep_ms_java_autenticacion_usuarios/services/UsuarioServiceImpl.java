package com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Comuna;
import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Usuario;
import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.repositories.ComunaRepository;
import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ComunaRepository comunaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository,
            ComunaRepository comunaRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.comunaRepository = comunaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> listarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {

        if (usuario.getComuna() == null ||
            usuario.getComuna().getId() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Debes indicar el ID de la comuna"
            );
        }

        Long comunaId = usuario.getComuna().getId();

        Comuna comuna = comunaRepository.findById(comunaId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No existe la comuna con ID: " + comunaId
            ));

        if (usuario.getContrasena() == null ||
            usuario.getContrasena().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Debes indicar una contraseña"
            );
        }

        usuario.setComuna(comuna);

        usuario.setContrasena(
            passwordEncoder.encode(usuario.getContrasena())
        );

        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizar(Long id, Usuario usuario) {

        Usuario existente = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Usuario no encontrado con ID: " + id
            ));

        if (usuario.getComuna() == null ||
            usuario.getComuna().getId() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Debes indicar el ID de la comuna"
            );
        }

        Long comunaId = usuario.getComuna().getId();

        Comuna comuna = comunaRepository.findById(comunaId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Comuna no encontrada con ID: " + comunaId
            ));

        existente.setRut(usuario.getRut());
        existente.setPnombre(usuario.getPnombre());
        existente.setSnombre(usuario.getSnombre());
        existente.setApellidos(usuario.getApellidos());
        existente.setEmail(usuario.getEmail());
        existente.setRol(usuario.getRol());
        existente.setActivo(usuario.getActivo());
        existente.setFechaNacimiento(usuario.getFechaNacimiento());
        existente.setDireccion(usuario.getDireccion());
        existente.setComuna(comuna);

        if (usuario.getContrasena() != null &&
            !usuario.getContrasena().isBlank()) {
            existente.setContrasena(
                passwordEncoder.encode(usuario.getContrasena())
            );
        }

        return usuarioRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Usuario no encontrado con ID: " + id
            );
        }

        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return usuarioRepository.existsById(id);
    }
}
