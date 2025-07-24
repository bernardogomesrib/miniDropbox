package com.mini_drive.drive.services;

import org.springframework.stereotype.Service;

import com.mini_drive.drive.controllers.requests.CompartilharRequest;
import com.mini_drive.drive.controllers.requests.PastaRequest;
import com.mini_drive.drive.controllers.requests.PesquisaPastaRequest;
import com.mini_drive.drive.controllers.requests.PesquisaPastaRequestCompartilhado;
import com.mini_drive.drive.controllers.requests.RenomearRequest;
import com.mini_drive.drive.entities.Arquivo;
import com.mini_drive.drive.entities.Pasta;
import com.mini_drive.drive.entities.PastaDTO;
import com.mini_drive.drive.entities.PastaDTO2;
import com.mini_drive.drive.entities.usuario.Usuario;
import com.mini_drive.drive.entities.usuario.UsuarioService;
import com.mini_drive.drive.exceptions.ResourceNotFoundException;
import com.mini_drive.drive.exceptions.UnauthorizedAccessException;
import com.mini_drive.drive.repositories.PastaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PastaService {
    private final PastaRepository pastaRepository;
    private final UsuarioService usuarioService;

    public Pasta pegarPastaPorId(String id, Usuario usuario) {
        Pasta p = pastaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasta não encontrada com o ID: " + id));
        if (p.getCreatedBy().getId().equals(usuario.getId()) || p.getCompartilhadoCom().contains(usuario.getEmail())) {
            return p;
        } else {
            throw new UnauthorizedAccessException("Você não tem permissão para acessar esta pasta.");
        }
    }

    public Pasta pegarPastaPorId(String id, Authentication authentication) {
        Pasta p = pastaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasta não encontrada com o ID: " + id));

        if (p.getCreatedBy().getId().equals(authentication.getName())) {
            return p;
        } else {
            throw new UnauthorizedAccessException("Você não tem permissão para acessar esta pasta.");
        }

    }

    public Pasta pegaPastaRaizMinha(Authentication authentication) {
        Usuario u = usuarioService.findUsuario(authentication);
        if (u.getPastaRaiz() != null) {
            return u.getPastaRaiz();
        } else {
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
        Page<Pasta> p = pastaRepository.findByCreatedBy(usuario, pageable);
        return p.map(PastaDTO::from);
    }

    public PastaDTO criarPasta(PastaRequest request, Authentication authentication) {
        if(request.getNome().equals("__root__")) {
            throw new IllegalArgumentException("Você não pode criar uma pasta com o nome '__root__'.");
        }
        Usuario u = usuarioService.findUsuario(authentication);
        return PastaDTO.from(pastaRepository.save(Pasta.builder()
                .nome(request.getNome())
                .pastaPai(!(request.getIdPastaPai() == null) ? pegarPastaPorId(request.getIdPastaPai(), u)
                        : u.getPastaRaiz())
                .build()));
    }

    public Page<PastaDTO> pesquisarPasta(PesquisaPastaRequest request, Pageable pageable,
            Authentication authentication) {
        Usuario usuario = usuarioService.findUsuario(authentication);

        if (request.getNomePasta() != null && !request.getNomePasta().isEmpty()
                && request.getPastaPaiId() != null && !request.getPastaPaiId().isEmpty()) {
            return pastaRepository.findByNomeContainingIgnoreCaseAndCreatedByAndPastaPaiId(
                    request.getNomePasta(), usuario, request.getPastaPaiId(), pageable).map(PastaDTO::from);
        } else if (request.getNomePasta() != null && !request.getNomePasta().isEmpty()) {
            return pastaRepository.findByNomeContainingIgnoreCaseAndCreatedBy(
                    request.getNomePasta(), usuario, pageable).map(PastaDTO::from);
        } else if (request.getPastaPaiId() != null && !request.getPastaPaiId().isEmpty()) {
            return pastaRepository.findByCreatedByAndPastaPaiId(
                    usuario, request.getPastaPaiId(), pageable).map(PastaDTO::from);
        } else {
            return pastaRepository.findByCreatedBy(usuario, pageable)
                    .map(PastaDTO::from);
        }
    }

    public Page<PastaDTO> pesquisarPastaCompartilhada(
            PesquisaPastaRequestCompartilhado request, Pageable pageable, Authentication authentication) {
        String emailUsuario = usuarioService.findUsuario(authentication).getEmail();

        if (request.getNomePasta() != null && !request.getNomePasta().isEmpty()
                && request.getIdDono() != null && !request.getIdDono().isEmpty()
                && request.getPastaPaiId() != null && !request.getPastaPaiId().isEmpty()) {
            return pastaRepository.findByNomeContainingIgnoreCaseAndCreatedByIdAndPastaPaiIdAndCompartilhadoComContains(
                    request.getNomePasta(), request.getIdDono(), request.getPastaPaiId(), emailUsuario, pageable)
                    .map(PastaDTO::from);
        }
        if (request.getNomePasta() != null && !request.getNomePasta().isEmpty()
                && request.getIdDono() != null && !request.getIdDono().isEmpty()) {
            return pastaRepository.findByNomeContainingIgnoreCaseAndCreatedByIdAndCompartilhadoComContains(
                    request.getNomePasta(), request.getIdDono(), emailUsuario, pageable).map(PastaDTO::from);
        }
        if (request.getNomePasta() != null && !request.getNomePasta().isEmpty()
                && request.getPastaPaiId() != null && !request.getPastaPaiId().isEmpty()) {
            return pastaRepository.findByNomeContainingIgnoreCaseAndPastaPaiIdAndCompartilhadoComContains(
                    request.getNomePasta(), request.getPastaPaiId(), emailUsuario, pageable).map(PastaDTO::from);
        }
        if (request.getIdDono() != null && !request.getIdDono().isEmpty()
                && request.getPastaPaiId() != null && !request.getPastaPaiId().isEmpty()) {
            return pastaRepository.findByCreatedByIdAndPastaPaiIdAndCompartilhadoComContains(
                    request.getIdDono(), request.getPastaPaiId(), emailUsuario, pageable).map(PastaDTO::from);
        }
        if (request.getNomePasta() != null && !request.getNomePasta().isEmpty()) {
            return pastaRepository.findByNomeContainingIgnoreCaseAndCompartilhadoComContains(
                    request.getNomePasta(), emailUsuario, pageable).map(PastaDTO::from);
        }
        if (request.getIdDono() != null && !request.getIdDono().isEmpty()) {
            return pastaRepository.findByCreatedByIdAndCompartilhadoComContains(
                    request.getIdDono(), emailUsuario, pageable).map(PastaDTO::from);
        }
        if (request.getPastaPaiId() != null && !request.getPastaPaiId().isEmpty()) {
            return pastaRepository.findByPastaPaiIdAndCompartilhadoComContains(
                    request.getPastaPaiId(), emailUsuario, pageable).map(PastaDTO::from);
        }
        return pastaRepository.findByCompartilhadoComContains(emailUsuario, pageable)
                .map(PastaDTO::from);
    }

    public PastaDTO renomearPasta(RenomearRequest request, Authentication authentication) {
        if (request.getNome().equals("__root__")) {
            throw new IllegalArgumentException("Você não pode renomear a pasta para '__root__'.");
        }
        Pasta p = pegarPastaPorId(request.getId(), usuarioService.findUsuario(authentication));
        p.setNome(request.getNome());
        return PastaDTO.from(pastaRepository.save(p));
    }

    public void apagarPasta(String id, Authentication authentication) {
        Usuario u = usuarioService.findUsuario(authentication);
        if (u.getPastaRaiz().getId().equals(id)) {
            throw new UnauthorizedAccessException("Você não pode apagar a pasta raiz.");
        }
        pastaRepository.delete(pegarPastaPorId(id, authentication));
    }

    public PastaDTO compartilhar(CompartilharRequest req, Authentication authentication) {
        Usuario u = usuarioService.findUsuario(authentication);
        if (u.getPastaRaiz().getId().equals(req.getId())) {
            throw new UnauthorizedAccessException("Você não pode compartilhar a pasta raiz.");
        }
        Pasta p = pegarPastaPorId(req.getId(), u);
        List<String> emails = p.getCompartilhadoCom();
        if (emails == null) {
            emails = new java.util.ArrayList<>();
        }
        if (emails.contains(req.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("A pasta já está compartilhada com este email.");
        } else {
            emails.add(req.getEmail());
            p.setCompartilhadoCom(emails);
            for(Pasta pasta : p.getSubpastas()){
                compartilhar(pasta, req.getEmail());
            }
            for(Arquivo arquivo : p.getArquivos()){
                if(arquivo.getCompartilhadoCom() == null){
                    arquivo.setCompartilhadoCom(new java.util.ArrayList<>());
                }
                if(!arquivo.getCompartilhadoCom().contains(req.getEmail().toLowerCase())){
                    arquivo.getCompartilhadoCom().add(req.getEmail());
                }
            }
            return PastaDTO.from(pastaRepository.save(p));
        }
    }

    public void compartilhar(Pasta pasta, String email){
        if (pasta.getCompartilhadoCom() == null) {
            pasta.setCompartilhadoCom(new java.util.ArrayList<>());
        }
        if (!pasta.getCompartilhadoCom().contains(email.toLowerCase())) {
            pasta.getCompartilhadoCom().add(email);
        }
        for (Pasta subpasta : pasta.getSubpastas()) {
            compartilhar(subpasta, email);
        }
        for (Arquivo arquivo : pasta.getArquivos()) {
            if (arquivo.getCompartilhadoCom() == null) {
                arquivo.setCompartilhadoCom(new java.util.ArrayList<>());
            }
            if (!arquivo.getCompartilhadoCom().contains(email.toLowerCase())) {
                arquivo.getCompartilhadoCom().add(email);
            }
        }
    }


    public List<Usuario> listarUsuariosCompartilhamento(String id, Authentication authentication) {
        Pasta pasta = pegarPastaPorId(id, usuarioService.findUsuario(authentication));
        List<String> compartilhados = pasta.getCompartilhadoCom();
        return usuarioService.findUsuariosByEmail(compartilhados);
    }

    public PastaDTO toDTO(Pasta pasta) {
        if (pasta == null) {
            return null;
        } else {
            return PastaDTO.from(pasta);
        }
    }
    public PastaDTO2 toDTO2(Pasta pasta){
        if(pasta == null){
            return null;
        } else {
            return PastaDTO2.from(pasta);
        }
    }
}
