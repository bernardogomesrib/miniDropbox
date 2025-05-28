package com.mini_drive.drive.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mini_drive.drive.entities.Arquivo;
import com.mini_drive.drive.entities.Pasta;
/* import com.mini_drive.drive.minio.MinIOInterfacing; */
import com.mini_drive.drive.repositories.ArquivoRepository;

import java.util.List;

import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArquivoService {
    private final ArquivoRepository arquivoRepository;
    private final PastaService pastaService;
    /* private final MinIOInterfacing minio; */
    public Arquivo salvar(MultipartFile arquivo, String pastaId,Authentication authentication) throws Exception {
        Pasta p = null;
        if(pastaId == null || pastaId.isEmpty()){
         p = pastaService.pegaPastaRaizMinha(authentication);
        }else{
            p = pastaService.pegarPastaPorId(pastaId, authentication);
        }
        return arquivoRepository.save(Arquivo.builder()
            .pasta(p)
            .arquivo(arquivo)
            .build());
    }
    public List<Arquivo> pegarTodosMeusArquivos(Authentication authentication) {
        return arquivoRepository.findAll().stream()
            .filter(a -> a.getCreatedBy().getId().equals(authentication.getName()))
            .toList();
    }
}
