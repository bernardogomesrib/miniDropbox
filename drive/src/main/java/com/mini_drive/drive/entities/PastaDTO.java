package com.mini_drive.drive.entities;

import java.util.List;

import com.mini_drive.drive.entities.base.BaseEntity;
import com.mini_drive.drive.entities.usuario.Usuario;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class PastaDTO extends BaseEntity {
    private String nome;

    private Usuario usuario;

    private List<Pasta> subpastas;

    private List<String> compartilhadoCom;

    private List<Arquivo> arquivos;

}
