package com.mini_drive.drive.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mini_drive.drive.controllers.requests.CompartilharRequest;
import com.mini_drive.drive.controllers.requests.CopiaArquivoRequest;
import com.mini_drive.drive.controllers.requests.PesquisaRequest;
import com.mini_drive.drive.controllers.requests.PesquisaRequestCompartilhado;
import com.mini_drive.drive.controllers.requests.RenomearRequest;
import com.mini_drive.drive.entities.ArquivoDTO;
import com.mini_drive.drive.services.ArquivoService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("arquivo")
@RequiredArgsConstructor
public class ArquivoController {
    private final ArquivoService service;

    /* private final MinIOInterfacing minio; */
    @PostMapping(value = "upload", consumes = "multipart/form-data")
    public ArquivoDTO uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam(value = "pastaId", required = false) String pastaId,
            Authentication authentication) throws Exception {
        return service.salvar(file, pastaId, authentication);
    }

    @GetMapping("pesquisa")
    public Page<ArquivoDTO> pesquisarArquivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String prop,
            @RequestParam(defaultValue = "DESC") String dir,
            PesquisaRequest pesquisaRequest,
            Authentication authentication)
            throws Exception {
        Sort.Direction direction = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<ArquivoDTO> pageResult = service.pesquisar(pesquisaRequest.getNomeArquivo(), pesquisaRequest.getPastaId(),
                pageable, authentication);
        return pageResult;
    }
    @GetMapping("pesquisa/compartilhados")
    public Page<ArquivoDTO> pesquisarCompartilhados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String prop,
            @RequestParam(defaultValue = "DESC") String dir,
            PesquisaRequestCompartilhado pesquisaRequest,
            Authentication authentication) throws Exception {
        Sort.Direction direction = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        return service.pesquisarCompartilhadoComigo(pesquisaRequest.getNomeArquivo(),pesquisaRequest.getPastaId(),
        pesquisaRequest.getIdDono(),authentication, pageable);
    }
    
    @PostMapping("copiar")
    public ArquivoDTO copiar(@RequestBody CopiaArquivoRequest entity,Authentication authentication) throws Exception {
        return service.copiar(entity.getArquivoId(), entity.getNomeArquivoDestino(),entity.getPastaDestinoId(), authentication);
    }
    
    @DeleteMapping("apagar")
    public void deleteFile(@RequestParam String id, Authentication authentication) throws Exception {
        service.deletar(id, authentication);
    }
    @PostMapping("compartilhar")
    public ArquivoDTO compartilhar(@RequestBody CompartilharRequest req, Authentication authentication) throws Exception {
        return service.compartilhar(req, authentication);
    }
    @PatchMapping("renomear")
    public ArquivoDTO renomear(RenomearRequest request, Authentication authentication) throws Exception {
        return service.renomear(request, authentication);
    }
} 
  