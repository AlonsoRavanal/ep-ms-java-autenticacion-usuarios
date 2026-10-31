package com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Region;
import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.services.RegionService;

@RestController
@RequestMapping("/api/regiones")
public class RegionRestController {

    private final RegionService regionService;

    public RegionRestController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public ResponseEntity<List<Region>> listar() {
        return ResponseEntity.ok(regionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> listarPorId(
            @PathVariable("id") Long id) {

        return regionService.listarPorId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Region> guardar(
            @RequestBody Region region) {

        Region nueva = regionService.guardar(region);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Region> actualizar(
            @PathVariable("id") Long id,
            @RequestBody Region region) {

        Region actualizada = regionService.actualizar(id, region);

        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(
            @PathVariable("id") Long id) {

        if (!regionService.existe(id)) {
            return ResponseEntity.notFound().build();
        }

        regionService.eliminarPorId(id);

        return ResponseEntity.noContent().build();
    }
}
