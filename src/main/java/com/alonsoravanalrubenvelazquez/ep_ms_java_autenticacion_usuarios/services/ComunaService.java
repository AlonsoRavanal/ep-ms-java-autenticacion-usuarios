package com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.services;

import java.util.List;
import java.util.Optional;

import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Comuna;

public interface ComunaService {

    List<Comuna> listar();
    Optional<Comuna> listarPorId(Long id);
    Comuna guardar(Comuna comuna);
    Comuna actualizar(Long id, Comuna comuna);
    void eliminarPorId(Long id);
    boolean existe(Long id);

}
