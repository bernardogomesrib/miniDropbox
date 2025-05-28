package com.mini_drive.drive.entities.usuario;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mini_drive.drive.entities.Arquivo;
import com.mini_drive.drive.entities.Pasta;
import com.mini_drive.drive.minio.MinIOInterfacing;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity(name = "usuario")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"pastaRaiz", "pastasCriadas", "pastasAtualizadas", "arquivosCriados", "arquivosAtualizados","minIOInterfacing"})
public class Usuario {
    @JsonIgnore
    @Transient
    @Autowired
    private MinIOInterfacing minIOInterfacing;
    private final static int LAST_ACTIVE_INTERVAL = 5;
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime lastSeenAt;

    @OneToOne(mappedBy = "usuario", cascade = jakarta.persistence.CascadeType.ALL)
    private Pasta pastaRaiz;

    @Transient
    public boolean isOnline() {
        return lastSeenAt != null && lastSeenAt.isAfter(LocalDateTime.now().minusMinutes(LAST_ACTIVE_INTERVAL));
    }

    @Transient
    public String getPfpUrl() throws Exception{
       return minIOInterfacing.pegarImagemDePerfil(this.id+"pfp",this.id);
    }
    @JsonIgnore
    @OneToMany(mappedBy = "createdBy")
    private List<Pasta> pastasCriadas;
    @JsonIgnore
    @OneToMany(mappedBy = "updatedBy")
    private List<Pasta> pastasAtualizadas;
    @JsonIgnore
    @OneToMany(mappedBy = "createdBy")
    private List<Arquivo> arquivosCriados;
    @JsonIgnore
    @OneToMany(mappedBy = "updatedBy")
    private List<Arquivo> arquivosAtualizados;
    
}
