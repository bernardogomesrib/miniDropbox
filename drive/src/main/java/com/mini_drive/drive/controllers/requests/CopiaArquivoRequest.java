package com.mini_drive.drive.controllers.requests;

import lombok.Data;

@Data
public class CopiaArquivoRequest {
    private String arquivoId;
    private String pastaDestinoId;
    private String nomeArquivoDestino;
}
