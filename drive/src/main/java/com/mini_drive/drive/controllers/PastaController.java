package com.mini_drive.drive.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mini_drive.drive.controllers.requests.PastaRequest;
import com.mini_drive.drive.entities.PastaDTO;
import com.mini_drive.drive.services.PastaService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/pasta")
@RequiredArgsConstructor
public class PastaController {
    private final PastaService service;
    @GetMapping("listar")
    public Page<PastaDTO> listarPastas(Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, 
            @RequestParam(defaultValue = "id") String prop,
            @RequestParam(defaultValue = "DESC") String dir
            ) {
            Sort.Direction direction = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        return service.listarPastasPorUsuario(authentication, pageable);
    }

    @PostMapping("criar")
    public PastaDTO criarPasta(@RequestBody PastaRequest request) {
        return service.criarPasta(request);
    }
    
}
