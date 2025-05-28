package com.mini_drive.drive.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import com.mini_drive.drive.entities.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, String> {

    Page<Arquivo> findByNomeContainingAndCreatedByIdAndPastaId(
            String nome, String idDono, String pastaId, Pageable pageable);

    Page<Arquivo> findByNomeContainingAndCreatedById(
            String nome, String idDono, Pageable pageable);

    Page<Arquivo> findByNomeContainingAndPastaId(
            String nome, String pastaId, Pageable pageable);

    Page<Arquivo> findByNomeContaining(
            String nome, Pageable pageable);

    Page<Arquivo> findByPastaId(String pastaId, Pageable pageable);

    Page<Arquivo> findByCreatedById(String idDono, Pageable pageable);

    Page<Arquivo> findByCreatedByIdAndPastaId(
            String idDono, String pastaId, Pageable pageable);

    Page<Arquivo> findByCreatedByIdAndNomeContaining(
            String idDono, String nome, Pageable pageable);

    Page<Arquivo> findByPastaIdAndNomeContaining(
            String pastaId, String nome, Pageable pageable);

    Page<Arquivo> findByPastaIdAndCreatedById(
            String pastaId, String idDono, Pageable pageable);

    Page<Arquivo> findByPastaIdAndCreatedByIdAndNomeContaining(
            String pastaId, String idDono, String nome, Pageable pageable);

    Page<Arquivo> findByNomeContainingAndCreatedByIdAndPastaIdAndCompartilhadoComContains(
            String nomeArquivo, String idDono, String pastaId, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByNomeContainingAndCreatedByIdAndCompartilhadoComContains(
                String nomeArquivo, String idDono, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByNomeContainingAndPastaIdAndCompartilhadoComContains(
                String nomeArquivo, String pastaId, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByCreatedByIdAndPastaIdAndCompartilhadoComContains(
                String idDono, String pastaId, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByNomeContainingAndCompartilhadoComContains(
                String nomeArquivo, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByCreatedByIdAndCompartilhadoComContains(
                String idDono, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByPastaIdAndCompartilhadoComContains(
                String pastaId, String compartilhadoCom, Pageable pageable);

    Page<Arquivo>findByCompartilhadoComContains(String compartilhadoCom, Pageable pageable);

}
