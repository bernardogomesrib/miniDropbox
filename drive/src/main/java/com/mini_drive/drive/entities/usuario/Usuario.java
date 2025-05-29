package com.mini_drive.drive.entities.usuario;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mini_drive.drive.entities.Arquivo;
import com.mini_drive.drive.entities.Pasta;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@ToString(exclude = { "pastaRaiz", "pastasCriadas", "pastasAtualizadas", "arquivosCriados", "arquivosAtualizados" })
public class Usuario {

    private final static int LAST_ACTIVE_INTERVAL = 5;
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime lastSeenAt;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "pasta_raiz_id")
    @JsonIgnore
    private Pasta pastaRaiz;

    @Transient
    public boolean isOnline() {
        return lastSeenAt != null && lastSeenAt.isAfter(LocalDateTime.now().minusMinutes(LAST_ACTIVE_INTERVAL));
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
