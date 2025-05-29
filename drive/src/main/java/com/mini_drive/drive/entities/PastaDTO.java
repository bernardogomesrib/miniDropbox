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
    "id", "nome", "usuario", "compartilhadoCom", 
    "createdAt", "updatedAt", "createdBy", "updatedBy"
})
public class PastaDTO  {

    private String id;
    private String nome;
    private Usuario usuario;
    private List<String> compartilhadoCom;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Usuario createdBy;
    private Usuario updatedBy;

    public static PastaDTO from(Pasta pasta) {
        return PastaDTO.builder()
                .id(pasta.getId())
                .nome(pasta.getNome())
                .usuario(pasta.getUsuario())
                .compartilhadoCom(pasta.getCompartilhadoCom())
                .createdAt(pasta.getCreatedAt())
                .updatedAt(pasta.getUpdatedAt())
                .createdBy(pasta.getCreatedBy())
                .updatedBy(pasta.getUpdatedBy())
                .build();
    }
    
}
