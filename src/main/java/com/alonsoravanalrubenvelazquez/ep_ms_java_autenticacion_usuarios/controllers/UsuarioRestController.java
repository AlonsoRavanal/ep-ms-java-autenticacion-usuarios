package com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.entities.Usuario;
import com.alonsoravanalrubenvelazquez.ep_ms_java_autenticacion_usuarios.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> listarPorId(
            @PathVariable("id") Long id) {

        return usuarioService.listarPorId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> guardar(
            @RequestBody Usuario usuario) {

        Usuario nuevo = usuarioService.guardar(usuario);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable("id") Long id,
            @RequestBody Usuario usuario) {

        Usuario actualizado = usuarioService.actualizar(id, usuario);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(
            @PathVariable("id") Long id) {

        if (!usuarioService.existe(id)) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.eliminarPorId(id);

        return ResponseEntity.noContent().build();
    }
}
