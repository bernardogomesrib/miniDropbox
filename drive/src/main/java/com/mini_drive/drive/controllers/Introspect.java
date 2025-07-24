package com.mini_drive.drive.controllers;

import org.springframework.web.bind.annotation.RestController;


import com.mini_drive.drive.entities.usuario.UsuarioService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequiredArgsConstructor
public class Introspect {
    private final UsuarioService usuarioService;

    @GetMapping("introspect")
    public Map<String,Object> introspect(@AuthenticationPrincipal Jwt jwt,Authentication authentication) throws Exception {
        Map<String, Object> claims = new HashMap<>(jwt.getClaims());
        claims.put("pfp",usuarioService.getImage(authentication));
        return claims;
    }
    
}
