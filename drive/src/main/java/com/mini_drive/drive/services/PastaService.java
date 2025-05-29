package com.mini_drive.drive.services;

import org.springframework.stereotype.Service;

import com.mini_drive.drive.controllers.requests.PastaRequest;
import com.mini_drive.drive.entities.Pasta;
import com.mini_drive.drive.entities.PastaDTO;
import com.mini_drive.drive.entities.usuario.Usuario;
import com.mini_drive.drive.entities.usuario.UsuarioService;
import com.mini_drive.drive.exceptions.UnauthorizedAccessException;
import com.mini_drive.drive.repositories.PastaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PastaService {
    private final PastaRepository pastaRepository;
    private final UsuarioService usuarioService;

    public Pasta pegarPastaPorId(String id,Usuario usuario) {
        Pasta p = pastaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasta não encontrada com o ID: " + id));
        if(p.getCreatedBy().getId().equals(usuario.getId())||p.getCompartilhadoCom().contains(usuario.getEmail())){
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
                    .usuario(u)
                    .build();
            novaPasta = pastaRepository.save(novaPasta);
            u.setPastaRaiz(novaPasta);
            usuarioService.save(u);
            return novaPasta;
        }
    }
    public Page<PastaDTO> listarPastasPorUsuario(Authentication authentication, Pageable pageable) {
        Usuario usuario = usuarioService.findUsuario(authentication);
        Page<Pasta> p =  pastaRepository.findByCreatedBy(usuario, pageable);
        return p.map(PastaDTO::from);
    }

	public PastaDTO criarPasta(PastaRequest request) {
        return PastaDTO.from(pastaRepository.save(Pasta.builder()
                .nome(request.getNome())
                .build()));
	}
   
}
