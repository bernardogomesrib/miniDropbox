package com.mini_drive.drive.entities.usuario;


import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mini_drive.drive.exceptions.ResourceNotFoundException;
import com.mini_drive.drive.minio.MinIOInterfacing;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final MinIOInterfacing minIOInterfacing;
    
    public Usuario findUsuario(Authentication authentication) {
        return usuarioRepository.findById(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
    }

    public Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email.toLowerCase()).orElse(null);
    }

    public List<Usuario> findUsuariosByEmail(List<String> emails) {
        return usuarioRepository.findByEmailInIgnoreCase(emails);
    }

    public PfpResponse image(Authentication authentication,@NotNull byte[] file,String mimetype) throws Exception {
        return new PfpResponse(minIOInterfacing.salvarProfilePicture(authentication.getName(), "pfp", file, mimetype));
    }
    public String getImage(Authentication authentication) throws Exception {
        return minIOInterfacing.pegarImagemDePerfil(authentication.getName(), "pfp");
    }
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
