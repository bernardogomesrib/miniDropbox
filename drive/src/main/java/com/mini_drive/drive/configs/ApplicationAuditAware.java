package com.mini_drive.drive.configs;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.mini_drive.drive.configs.interceptor.UserMapper;
import com.mini_drive.drive.entities.usuario.Usuario;


public class ApplicationAuditAware implements AuditorAware<Usuario> {
    @Autowired
    private UserMapper userMapper;
    @Override
    @NonNull
    public Optional<Usuario> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        

        return Optional.ofNullable(userMapper.toUsuario(jwtAuthenticationToken.getToken().getClaims()));
    }
}