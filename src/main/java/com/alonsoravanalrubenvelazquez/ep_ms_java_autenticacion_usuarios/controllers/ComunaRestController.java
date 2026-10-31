package com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Comuna;
import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.services.ComunaService;

@RestController
@RequestMapping("/api/comunas")
public class ComunaRestController {

    private final ComunaService comunaService;

    public ComunaRestController(ComunaService comunaService) {
        this.comunaService = comunaService;
    }

    @GetMapping
    public ResponseEntity<List<Comuna>> listar() {
        return ResponseEntity.ok(comunaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comuna> listarPorId(
            @PathVariable("id") Long id) {

        return comunaService.listarPorId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comuna> guardar(
            @RequestBody Comuna comuna) {

        Comuna nueva = comunaService.guardar(comuna);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comuna> actualizar(
            @PathVariable("id") Long id,
            @RequestBody Comuna comuna) {

        Comuna actualizada = comunaService.actualizar(id, comuna);

        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(
            @PathVariable("id") Long id) {

        if (!comunaService.existe(id)) {
            return ResponseEntity.notFound().build();
        }

        comunaService.eliminarPorId(id);

        return ResponseEntity.noContent().build();
    }
}
