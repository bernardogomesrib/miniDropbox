package com.mini_drive.drive.controllers;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mini_drive.drive.entities.ArquivoDTO;
import com.mini_drive.drive.services.ArquivoService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;




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
    public Page<ArquivoDTO> pesquisarArquivos(Authentication authentication) throws Exception {
        return service.pesquisar(null, null, null, authentication);
    }
    
}
