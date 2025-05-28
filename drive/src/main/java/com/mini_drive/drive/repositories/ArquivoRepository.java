package com.mini_drive.drive.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mini_drive.drive.entities.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, String> {
    
}
