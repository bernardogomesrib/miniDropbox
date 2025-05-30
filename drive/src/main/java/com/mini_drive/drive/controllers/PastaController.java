package com.mini_drive.drive.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mini_drive.drive.controllers.requests.CompartilharRequest;
import com.mini_drive.drive.controllers.requests.PastaRequest;
import com.mini_drive.drive.controllers.requests.PesquisaPastaRequest;
import com.mini_drive.drive.controllers.requests.PesquisaPastaRequestCompartilhado;
import com.mini_drive.drive.controllers.requests.RenomearRequest;
import com.mini_drive.drive.entities.Pasta;
import com.mini_drive.drive.entities.PastaDTO;
import com.mini_drive.drive.entities.PastaDTO2;
import com.mini_drive.drive.entities.usuario.Usuario;
import com.mini_drive.drive.services.PastaService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/pasta")
@RequiredArgsConstructor
public class PastaController {
    private final PastaService service;

    @GetMapping("root")
    public Pasta getRoot(Authentication authentication) {
        return service.pegaPastaRaizMinha(authentication);
    }
    @GetMapping("entrar/{id}")
    public PastaDTO2 entrarPasta(@RequestParam String id, Authentication authentication) {
        return service.toDTO2(service.pegarPastaPorId(id, authentication));
    }
    @GetMapping("listar/usuarios/{id}")
    public List<Usuario> listarUsuariosCompartilhamento(@RequestParam String id, Authentication authentication) {
        return service.listarUsuariosCompartilhamento(id, authentication);
    }
    
    
    @GetMapping("listar")
    public Page<PastaDTO> listarPastas(Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String prop,
            @RequestParam(defaultValue = "DESC") String dir) {
        Sort.Direction direction = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        return service.listarPastasPorUsuario(authentication, pageable);
    }

    @PostMapping("criar")
    public PastaDTO criarPasta(@RequestBody PastaRequest request, Authentication authentication) {
        return service.criarPasta(request, authentication);
    }

    @GetMapping("pesquisar")
    public Page<PastaDTO> pesquisar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String prop,
            @RequestParam(defaultValue = "DESC") String dir,
            PesquisaPastaRequest request,
            Authentication authentication) {
        Sort.Direction direction = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        return service.pesquisarPasta(request, pageable, authentication);
    }
    @GetMapping("pesquisar/compartilhados")
    public Page<PastaDTO> pesquisarCompartilhado(
        @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String prop,
            @RequestParam(defaultValue = "DESC") String dir,
            PesquisaPastaRequestCompartilhado request,
            Authentication authentication
    ) {
        Sort.Direction direction = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        return service.pesquisarPastaCompartilhada(request, pageable, authentication);
    }
    
    @PutMapping("renomear")
    public PastaDTO renomear(RenomearRequest request, Authentication authentication) {
        
        return service.renomearPasta(request, authentication);
        
    }
    @DeleteMapping("apagar")
    public void apagar(@RequestParam String id,Authentication authentication){
        service.apagarPasta(id,authentication);
    }

    @PostMapping("compartilhar")
    public PastaDTO compartilhar(CompartilharRequest req, Authentication authentication) {
        return service.compartilhar(req, authentication);
    }
}
