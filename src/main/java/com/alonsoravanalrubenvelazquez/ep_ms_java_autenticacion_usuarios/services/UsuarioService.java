package com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.services;

import java.util.List;
import java.util.Optional;

import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Usuario;

public interface UsuarioService {

    List<Usuario> listar();
    Optional<Usuario> listarPorId(Long id);
    Usuario guardar(Usuario usuario);
    Usuario actualizar(Long id, Usuario usuario);
    void eliminarPorId(Long id);
    boolean existe(Long id);

}
