package com.mini_drive.drive.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mini_drive.drive.entities.Arquivo;
import com.mini_drive.drive.minio.MinIOInterfacing;
import com.mini_drive.drive.services.ArquivoService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("Arquivo")
@RequiredArgsConstructor
public class ArquivoController {
    private final ArquivoService service;
    private final MinIOInterfacing minio;
    @PostMapping(value = "upload", consumes = "multipart/form-data")
    public Arquivo uploadFile(@RequestParam("file") MultipartFile file,
                                              @RequestParam(value = "pastaId", required = false) String pastaId,
                                              Authentication authentication) throws Exception {
        return service.salvar(file, pastaId, authentication);
    }
    @PostMapping(value = "path", consumes = "multipart/form-data")
    public String postMethodName(@RequestParam("file")MultipartFile file) throws Exception {
        
        
        return minio.uploadFile("defaultpfp","teste",file);
    }
    
}
