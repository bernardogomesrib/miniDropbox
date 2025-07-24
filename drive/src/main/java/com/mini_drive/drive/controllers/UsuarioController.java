package com.mini_drive.drive.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini_drive.drive.entities.usuario.Usuario;
import com.mini_drive.drive.entities.usuario.UsuarioService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("usuarios")
    public List<Usuario> listarUsuarios(@RequestBody List<String> emails) {
        return service.findUsuariosByEmail(emails);
    }
}
