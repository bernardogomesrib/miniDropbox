package com.mini_drive.drive.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mini_drive.drive.entities.base.BaseEntity;
import com.mini_drive.drive.minio.MinIOInterfacing;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Arquivo extends BaseEntity{
    @Autowired
    @Transient
    @JsonIgnore
    private MinIOInterfacing minIOInterfacing;
    private String nome;
    private String tipo;
    private long tamanho;
    @ManyToOne
    @JoinColumn(name = "pasta_id")
    private Pasta pasta;
    @Transient
    public String getUrl() throws Exception {
        if (pasta != null) {
            return minIOInterfacing.getUrl(pasta.getNome(), this.nome);
        }
        return null;
    }
    @JsonIgnore
    @Transient
    private MultipartFile arquivo;

    public void setArquivo(MultipartFile arquivo) {
        this.arquivo = arquivo;
        if (arquivo != null) {
            this.nome = arquivo.getOriginalFilename();
            this.tipo = arquivo.getContentType();
            this.tamanho = arquivo.getSize();
            try {
                minIOInterfacing.uploadFile(pasta.getNome(), this.nome, arquivo);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao fazer upload do arquivo: " + e.getMessage(), e);
            }
        }

    }
}
