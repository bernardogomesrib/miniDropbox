package com.mini_drive.drive.entities;

import java.util.List;


import com.mini_drive.drive.entities.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Arquivo extends BaseEntity{
    
    private String nome;
    private String tipo;
    private long tamanho;
    private List<String> compartilhadoCom;
    @ManyToOne
    @JoinColumn(name = "pasta_id")    
    private Pasta pasta;



}
