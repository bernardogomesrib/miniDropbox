package com.mini_drive.drive.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mini_drive.drive.entities.Pasta;
import com.mini_drive.drive.entities.usuario.Usuario;

public interface PastaRepository extends JpaRepository<Pasta, String> {

    Page<Pasta> findByCreatedBy(Usuario usuario, Pageable pageable);
    
     Page<Pasta> findByNomeContainingIgnoreCaseAndCreatedByAndPastaPaiId(String nome, Usuario usuario, String pastaPaiId, Pageable pageable);

    Page<Pasta> findByNomeContainingIgnoreCaseAndCreatedBy(String nome, Usuario usuario, Pageable pageable);

    Page<Pasta> findByCreatedByAndPastaPaiId(Usuario usuario, String pastaPaiId, Pageable pageable);

    // Métodos para pesquisa compartilhada
    Page<Pasta> findByNomeContainingIgnoreCaseAndCreatedByIdAndPastaPaiIdAndCompartilhadoComContains(
            String nome, String idDono, String pastaPaiId, String email, Pageable pageable);

    Page<Pasta> findByNomeContainingIgnoreCaseAndCreatedByIdAndCompartilhadoComContains(
            String nome, String idDono, String email, Pageable pageable);

    Page<Pasta> findByNomeContainingIgnoreCaseAndPastaPaiIdAndCompartilhadoComContains(
            String nome, String pastaPaiId, String email, Pageable pageable);

    Page<Pasta> findByCreatedByIdAndPastaPaiIdAndCompartilhadoComContains(
            String idDono, String pastaPaiId, String email, Pageable pageable);

    Page<Pasta> findByNomeContainingIgnoreCaseAndCompartilhadoComContains(
            String nome, String email, Pageable pageable);

    Page<Pasta> findByCreatedByIdAndCompartilhadoComContains(
            String idDono, String email, Pageable pageable);

    Page<Pasta> findByPastaPaiIdAndCompartilhadoComContains(
            String pastaPaiId, String email, Pageable pageable);

    Page<Pasta> findByCompartilhadoComContains(
            String email, Pageable pageable);
}
