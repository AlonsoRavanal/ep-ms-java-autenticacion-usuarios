package com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Region;
import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.repositories.RegionRepository;

@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Region> listar() {
        return regionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Region> listarPorId(Long id) {
        return regionRepository.findById(id);
    }

    @Override
    @Transactional
    public Region guardar(Region region) {
        return regionRepository.save(region);
    }

    @Override
    @Transactional
    public Region actualizar(Long id, Region region) {
        Region existente = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region no encontrada con id: " + id));

        existente.setNombre(region.getNombre());

        return regionRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        regionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return regionRepository.existsById(id);
    }

}
