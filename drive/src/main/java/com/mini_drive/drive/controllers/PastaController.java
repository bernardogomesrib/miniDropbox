package com.mini_drive.drive.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini_drive.drive.entities.Pasta;
import com.mini_drive.drive.repositories.PastaRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pasta")
@RequiredArgsConstructor
public class PastaController {
    private final PastaRepository pastaRepository;
    @GetMapping("/listar")
    public List<Pasta> listarPastas() {
        return pastaRepository.findAll();
    }

}
