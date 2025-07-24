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
    "id", "nome", "compartilhadoCom","subpastas","arquivos","pastaPai",
    "createdAt", "updatedAt", "createdBy", "updatedBy"
})
public class PastaDTO2  {

    private String id;
    private String nome;
    private Usuario usuario;
    private List<PastaDTO> subpastas;
    private List<Arquivo> arquivos;
    private List<String> compartilhadoCom;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PastaDTO pastaPai;
    private Usuario createdBy;
    private Usuario updatedBy;
    
    public static PastaDTO2 from(Pasta pasta) {
        return PastaDTO2.builder()
                .id(pasta.getId())
                .nome(pasta.getNome())
                .usuario(pasta.getUsuario())
                .subpastas(pasta.getSubpastas().stream().map(PastaDTO::from).toList())
                .arquivos(pasta.getArquivos())
                .pastaPai(PastaDTO.from(pasta.getPastaPai()))
                .compartilhadoCom(pasta.getCompartilhadoCom())
                .createdAt(pasta.getCreatedAt())
                .updatedAt(pasta.getUpdatedAt())
                .createdBy(pasta.getCreatedBy())
                .updatedBy(pasta.getUpdatedBy())
                .build();
    }
    
}
