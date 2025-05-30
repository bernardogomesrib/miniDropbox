package com.mini_drive.drive.entities.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findById(String id);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByEmailInIgnoreCase(List<String> emails);
    
}
