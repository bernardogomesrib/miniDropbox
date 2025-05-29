package com.mini_drive.drive.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mini_drive.drive.entities.usuario.Usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({
    "id", "nome", "tipo", "tamanho", "compartilhadoCom", "pasta", "url",
    "createdAt", "updatedAt", "createdBy", "updatedBy"
})
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
