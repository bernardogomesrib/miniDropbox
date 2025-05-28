package com.mini_drive.drive.entities;

import java.time.LocalDateTime;
import java.util.List;


import com.mini_drive.drive.entities.usuario.Usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArquivoDTO {
    private String Id;
    private String nome;
    private String tipo;
    private long tamanho;
    private List<String> compartilhadoCom;
    private Pasta pasta;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Usuario createdBy;
    private Usuario updatedBy;
}
