package com.mini_drive.drive.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import com.mini_drive.drive.entities.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, String> {

    Page<Arquivo> findByNomeContainingIgnoreCaseAndCreatedByIdAndPastaId(
            String nome, String idDono, String pastaId, Pageable pageable);

    Page<Arquivo> findByNomeContainingIgnoreCaseAndCreatedById(
            String nome, String idDono, Pageable pageable);

    Page<Arquivo> findByNomeContainingIgnoreCaseAndPastaId(
            String nome, String pastaId, Pageable pageable);

    Page<Arquivo> findByNomeContainingIgnoreCase(
            String nome, Pageable pageable);

    Page<Arquivo> findByPastaId(String pastaId, Pageable pageable);

    Page<Arquivo> findByCreatedById(String idDono, Pageable pageable);

    Page<Arquivo> findByCreatedByIdAndPastaId(
            String idDono, String pastaId, Pageable pageable);

    Page<Arquivo> findByCreatedByIdAndNomeContainingIgnoreCase(
            String idDono, String nome, Pageable pageable);

    Page<Arquivo> findByPastaIdAndNomeIgnoreCaseContaining(
            String pastaId, String nome, Pageable pageable);

    Page<Arquivo> findByPastaIdAndCreatedById(
            String pastaId, String idDono, Pageable pageable);

    Page<Arquivo> findByPastaIdAndCreatedByIdAndNomeContainingIgnoreCase(
            String pastaId, String idDono, String nome, Pageable pageable);

    Page<Arquivo> findByNomeContainingIgnoreCaseAndCreatedByIdAndPastaIdAndCompartilhadoComContains(
            String nomeArquivo, String idDono, String pastaId, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByNomeContainingIgnoreCaseAndCreatedByIdAndCompartilhadoComContains(
                String nomeArquivo, String idDono, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByNomeContainingIgnoreCaseAndPastaIdAndCompartilhadoComContains(
                String nomeArquivo, String pastaId, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByCreatedByIdAndPastaIdAndCompartilhadoComContains(
                String idDono, String pastaId, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByNomeContainingIgnoreCaseAndCompartilhadoComContains(
                String nomeArquivo, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByCreatedByIdAndCompartilhadoComContains(
                String idDono, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByPastaIdAndCompartilhadoComContains(
                String pastaId, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByCompartilhadoComContains(String compartilhadoCom, Pageable pageable);

}
