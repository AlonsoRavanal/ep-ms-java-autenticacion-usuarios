package com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.services;

import java.util.List;
import java.util.Optional;

import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Region;

public interface RegionService {

    List<Region> listar();
    Optional<Region> listarPorId(Long id);
    Region guardar(Region region);
    Region actualizar(Long id, Region region);
    void eliminarPorId(Long id);
    boolean existe(Long id);

}
