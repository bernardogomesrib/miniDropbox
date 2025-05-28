package com.mini_drive.drive.services;

import org.springframework.stereotype.Service;

import com.mini_drive.drive.entities.Pasta;
import com.mini_drive.drive.entities.usuario.Usuario;
import com.mini_drive.drive.entities.usuario.UsuarioService;
import com.mini_drive.drive.exceptions.UnauthorizedAccessException;
import com.mini_drive.drive.repositories.PastaRepository;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PastaService {
    private final PastaRepository pastaRepository;
    private final UsuarioService usuarioService;

    public Pasta pegarPastaPorId(String id,Authentication authentication) {
        Pasta p = pastaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasta não encontrada com o ID: " + id));
        if(p.getCreatedBy().getId().equals(authentication.getName())){
            return p;
        } else {
            throw new UnauthorizedAccessException("Você não tem permissão para acessar esta pasta.");
        }
    }

    public Pasta pegaPastaRaizMinha(Authentication authentication) {
        Usuario u = usuarioService.findUsuario(authentication);
        if(u.getPastaRaiz() != null) {
            return u.getPastaRaiz();
        }else{
            Pasta novaPasta = Pasta.builder()
                    .nome("__root__")
                    .build();
            novaPasta = pastaRepository.save(novaPasta);
            u.setPastaRaiz(novaPasta);
            usuarioService.save(u);
            return novaPasta;
        }
    }
    
}
