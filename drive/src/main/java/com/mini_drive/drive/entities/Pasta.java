package com.mini_drive.drive.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mini_drive.drive.entities.base.BaseEntity;
import com.mini_drive.drive.entities.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Pasta extends BaseEntity {
    private String nome;
    @OneToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;
    @OneToMany(mappedBy = "pastaPai", cascade = CascadeType.ALL)
    private List<Pasta> subpastas;

    private List<String> compartilhadoCom;

    @ManyToOne
    @JoinColumn(name = "pasta_pai_id")
    private Pasta pastaPai;

    @OneToMany(mappedBy = "pasta", cascade = CascadeType.ALL)
    private List<Arquivo> arquivos;

    
    
    
}
