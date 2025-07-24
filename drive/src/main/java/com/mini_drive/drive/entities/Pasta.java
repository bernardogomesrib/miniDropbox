package com.mini_drive.drive.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mini_drive.drive.entities.base.BaseEntity;
import com.mini_drive.drive.entities.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
    "id", "nome","subpastas","arquivos","pastaPai",
    "updatedAt"
})
public class Pasta extends BaseEntity {
    @Column(nullable = false)
    private String nome;
    @OneToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;
    @OneToMany(mappedBy = "pastaPai", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Pasta> subpastas;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> compartilhadoCom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pasta_pai_id")
    @JsonBackReference
    private Pasta pastaPai;

    @JsonManagedReference
    @OneToMany(mappedBy = "pasta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Arquivo> arquivos;

}
