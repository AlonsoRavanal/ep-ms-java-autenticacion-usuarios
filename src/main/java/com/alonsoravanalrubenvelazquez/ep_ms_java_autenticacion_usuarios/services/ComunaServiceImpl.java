package com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Comuna;
import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Region;
import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.repositories.ComunaRepository;
import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.repositories.RegionRepository;

@Service
public class ComunaServiceImpl implements ComunaService{

    private final ComunaRepository comunaRepository;
    private final RegionRepository regionRepository;

    public ComunaServiceImpl(
            ComunaRepository comunaRepository,
            RegionRepository regionRepository) {

        this.comunaRepository = comunaRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comuna> listar() {
        return comunaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comuna> listarPorId(Long id) {
        return comunaRepository.findById(id);
    }

    @Override
    @Transactional
    public Comuna guardar(Comuna comuna) {

        if (comuna.getRegion() == null ||
            comuna.getRegion().getId() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Debes indicar el ID de la región"
            );
        }

        Long regionId = comuna.getRegion().getId();

        Region region = regionRepository.findById(regionId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No existe la región con ID: " + regionId
            ));

        comuna.setRegion(region);

        return comunaRepository.save(comuna);
    }

    
    @Override
    @Transactional
    public Comuna actualizar(Long id, Comuna comuna) {

        Comuna existente = comunaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Comuna no encontrada con ID: " + id
            ));

        if (comuna.getRegion() == null ||
            comuna.getRegion().getId() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Debes indicar el ID de la región"
            );
        }

        Long regionId = comuna.getRegion().getId();

        Region region = regionRepository.findById(regionId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Región no encontrada con ID: " + regionId
            ));

        existente.setNombre(comuna.getNombre());
        existente.setRegion(region);

        return comunaRepository.save(existente);
    }


    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        comunaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return comunaRepository.existsById(id);
    }

}
