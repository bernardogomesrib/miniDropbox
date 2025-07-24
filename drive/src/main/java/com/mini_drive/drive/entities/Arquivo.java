package com.mini_drive.drive.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mini_drive.drive.entities.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
    "id", "nome", "tipo", "tamanho", "compartilhadoCom", "pasta",
    "createdAt", "updatedAt", "createdBy", "updatedBy"
})
public class Arquivo extends BaseEntity {
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String tipo;
    @Column(nullable = false)
    private long tamanho;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> compartilhadoCom;

    @JoinColumn(name = "pasta_id")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Pasta pasta;

}
