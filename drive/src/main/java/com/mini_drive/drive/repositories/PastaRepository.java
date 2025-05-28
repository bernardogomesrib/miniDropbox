package com.mini_drive.drive.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mini_drive.drive.entities.Pasta;

public interface PastaRepository extends JpaRepository<Pasta, String> {
    
}
