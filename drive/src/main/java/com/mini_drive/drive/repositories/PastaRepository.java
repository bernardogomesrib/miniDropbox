package com.mini_drive.drive.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mini_drive.drive.entities.Pasta;
import com.mini_drive.drive.entities.usuario.Usuario;

public interface PastaRepository extends JpaRepository<Pasta, String> {

    Page<Pasta> findByCreatedBy(Usuario usuario, Pageable pageable);
    
}
