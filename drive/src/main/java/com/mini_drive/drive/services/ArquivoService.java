package com.mini_drive.drive.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mini_drive.drive.controllers.requests.CompartilharRequest;
import com.mini_drive.drive.controllers.requests.RenomearRequest;
import com.mini_drive.drive.entities.Arquivo;
import com.mini_drive.drive.entities.ArquivoDTO;
import com.mini_drive.drive.entities.Pasta;
import com.mini_drive.drive.entities.usuario.Usuario;
import com.mini_drive.drive.entities.usuario.UsuarioService;
import com.mini_drive.drive.exceptions.UnauthorizedAccessException;
import com.mini_drive.drive.minio.MinIOInterfacing;
import com.mini_drive.drive.repositories.ArquivoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArquivoService {
    private final ArquivoRepository arquivoRepository;
    private final PastaService pastaService;
    private final UsuarioService usuarioService;
    private final MinIOInterfacing minio;

    public Arquivo pegarArquivoPorId(String id, Usuario usuario) {
        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado com o ID: " + id));

        if (arquivo.getCreatedBy().getId().equals(usuario.getId())
                || (arquivo.getCompartilhadoCom() != null && arquivo.getCompartilhadoCom().contains(usuario.getEmail()))) {
            return arquivo;
        } else {
            throw new UnauthorizedAccessException("Você não tem permissão para acessar este arquivo.");
        }
    }

    public Arquivo pegarArquivoPorId(String id, Authentication authentication) {
        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado com o ID: " + id));

        if (arquivo.getCreatedBy().getId().equals(authentication.getName())) {
            return arquivo;
        } else {
            throw new UnauthorizedAccessException("Você não tem permissão para acessar este arquivo.");
        }
    }

    public Page<ArquivoDTO> toPageDTO(Page<Arquivo> arquivos) throws Exception {
        return arquivos.map(arquivo -> {
            try {
                return toDTO(arquivo);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao converter Arquivo para ArquivoDTO", e);
            }
        });
    }

    public ArquivoDTO toDTO(Arquivo arquivo) throws Exception {
        return ArquivoDTO.builder()
                .createdAt(arquivo.getCreatedAt())
                .updatedAt(arquivo.getUpdatedAt())
                .createdBy(arquivo.getCreatedBy())
                .updatedBy(arquivo.getUpdatedBy())
                .compartilhadoCom(arquivo.getCompartilhadoCom())
                .pasta(arquivo.getPasta())
                .url(minio.getUrl(arquivo.getPasta().getCreatedBy().getId(), arquivo.getId()))
                .compartilhadoCom(arquivo.getCompartilhadoCom())
                .Id(arquivo.getId())
                .nome(arquivo.getNome())
                .tipo(arquivo.getTipo())
                .tamanho(arquivo.getTamanho())
                .build();

    }

    @Transactional
    public ArquivoDTO salvar(MultipartFile arquivo, String pastaId, Authentication authentication) throws Exception {
        Pasta p = (pastaId == null || pastaId.isEmpty())
                ? pastaService.pegaPastaRaizMinha(authentication)
                : pastaService.pegarPastaPorId(pastaId, usuarioService.findUsuario(authentication));

        // 1. Salva o Arquivo no banco para gerar o ID
        Arquivo entidade = Arquivo.builder()
                .pasta(p)
                .nome(arquivo.getOriginalFilename())
                .tipo(arquivo.getContentType())
                .tamanho(arquivo.getSize())
                .build();
        Arquivo salvo = arquivoRepository.save(entidade);
        try {
            // 2. Faz upload no MinIO usando o ID como nome
            minio.uploadFile(p.getCreatedBy().getId(), salvo.getId(), arquivo);
            // 3. Atualiza o nomeUnico no banco, se quiser guardar essa informação
            return toDTO(salvo);
        } catch (Exception e) {
            // Se o upload falhar, lança exceção para o Spring fazer rollback no banco
            throw e;
        }
    }

    public Page<ArquivoDTO> pesquisar(String nomeArquivo, String pastaId, Pageable pageable,
            Authentication authentication) throws Exception {
        String idDono = authentication.getName();

        if (nomeArquivo != null && !nomeArquivo.isEmpty() && pastaId != null && !pastaId.isEmpty()) {
            return toPageDTO(
                    arquivoRepository.findByNomeContainingAndCreatedByIdAndPastaId(nomeArquivo, idDono, pastaId,
                            pageable));
        } else if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
            return toPageDTO(arquivoRepository.findByNomeContainingAndCreatedById(nomeArquivo, idDono, pageable));
        } else if (pastaId != null && !pastaId.isEmpty()) {
            return toPageDTO(arquivoRepository.findByCreatedByIdAndPastaId(idDono, pastaId, pageable));
        } else {
            return toPageDTO(arquivoRepository.findByCreatedById(idDono, pageable));
        }
    }

    public Page<ArquivoDTO> pesquisarCompartilhadoComigo(
            String nomeArquivo,
            String pastaId,
            String idDono,
            Authentication authentication,
            Pageable pageable) throws Exception {

        String compartilhadoCom = usuarioService.findUsuario(authentication).getEmail();

        if (nomeArquivo != null && !nomeArquivo.isEmpty()
                && idDono != null && !idDono.isEmpty()
                && pastaId != null && !pastaId.isEmpty()) {
            return toPageDTO(arquivoRepository.findByNomeContainingAndCreatedByIdAndPastaIdAndCompartilhadoComContains(
                    nomeArquivo, idDono, pastaId, compartilhadoCom, pageable));
        }
        if (nomeArquivo != null && !nomeArquivo.isEmpty()
                && idDono != null && !idDono.isEmpty()) {
            return toPageDTO(arquivoRepository.findByNomeContainingAndCreatedByIdAndCompartilhadoComContains(
                    nomeArquivo, idDono, compartilhadoCom, pageable));
        }
        if (nomeArquivo != null && !nomeArquivo.isEmpty()
                && pastaId != null && !pastaId.isEmpty()) {
            return toPageDTO(arquivoRepository.findByNomeContainingAndPastaIdAndCompartilhadoComContains(
                    nomeArquivo, pastaId, compartilhadoCom, pageable));
        }
        if (idDono != null && !idDono.isEmpty()
                && pastaId != null && !pastaId.isEmpty()) {
            return toPageDTO(arquivoRepository.findByCreatedByIdAndPastaIdAndCompartilhadoComContains(
                    idDono, pastaId, compartilhadoCom, pageable));
        }
        if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
            return toPageDTO(arquivoRepository.findByNomeContainingAndCompartilhadoComContains(
                    nomeArquivo, compartilhadoCom, pageable));
        }
        if (idDono != null && !idDono.isEmpty()) {
            return toPageDTO(arquivoRepository.findByCreatedByIdAndCompartilhadoComContains(
                    idDono, compartilhadoCom, pageable));
        }
        if (pastaId != null && !pastaId.isEmpty()) {
            return toPageDTO(arquivoRepository.findByPastaIdAndCompartilhadoComContains(
                    pastaId, compartilhadoCom, pageable));
        }
        return toPageDTO(arquivoRepository.findByCompartilhadoComContains(compartilhadoCom, pageable));
    }

    public void deletar(String id, Authentication authentication) throws Exception {
        String idDono = authentication.getName();
        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));

        if (!arquivo.getCreatedBy().getId().equals(idDono)) {
            throw new UnauthorizedAccessException("Você não tem permissão para deletar este arquivo");
        }

        // Deleta o arquivo do MinIO
        minio.deleteFile(arquivo.getPasta().getCreatedBy().getId(), arquivo.getId());

        // Deleta o registro do banco
        arquivoRepository.delete(arquivo);
    }

    @Transactional
    public ArquivoDTO copiar(String arquivoId, String nomeArquivoDestino, String pastaDestinoId,
            Authentication authentication) throws Exception {
        Usuario usuario = usuarioService.findUsuario(authentication);
        Arquivo arquivo = arquivoRepository.findById(arquivoId)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado com o ID: " + arquivoId));
        Pasta pastaDoArquivo = arquivo.getPasta();


        boolean ehDonoArquivo = arquivo.getCreatedBy().getId().equals(usuario.getId());
        boolean arquivoCompartilhadoComUsuario = arquivo.getCompartilhadoCom() != null && arquivo.getCompartilhadoCom().contains(usuario.getEmail());
        boolean ehDonoPasta = pastaDoArquivo.getCreatedBy().getId().equals(usuario.getId());
        boolean pastaCompartilhadaComUsuario = pastaDoArquivo.getCompartilhadoCom() != null && pastaDoArquivo.getCompartilhadoCom().contains(usuario.getEmail());

        if (!(ehDonoArquivo || arquivoCompartilhadoComUsuario || ehDonoPasta || pastaCompartilhadaComUsuario)) {
            throw new UnauthorizedAccessException("Você não tem permissão para copiar este arquivo");
        }

        Pasta pastaDestino = (pastaDestinoId == null || pastaDestinoId.isEmpty())
                ? pastaService.pegaPastaRaizMinha(authentication)
                : pastaService.pegarPastaPorId(pastaDestinoId, usuario);
        
        Arquivo copiaArquivo = arquivoRepository.save(Arquivo.builder()
                .nome(nomeArquivoDestino != null && !nomeArquivoDestino.isEmpty() ? nomeArquivoDestino : "Cópia de "+arquivo.getNome())
                .tipo(arquivo.getTipo())
                .tamanho(arquivo.getTamanho())
                .pasta(pastaDestino)
                .build());
        minio.copiarArquivo(
                pastaDoArquivo.getCreatedBy().getId(), arquivo.getId(),
                pastaDestino.getCreatedBy().getId(), copiaArquivo.getId());
        return toDTO(copiaArquivo);
    }

	public ArquivoDTO compartilhar(CompartilharRequest req, Authentication authentication) throws Exception {
        Arquivo arquivo = pegarArquivoPorId(req.getId(),  usuarioService.findUsuario(authentication));
        List<String> emails = arquivo.getCompartilhadoCom();
        if(emails == null){
            emails = new java.util.ArrayList<>();
        }
        if(!emails.contains(req.getEmail())){
            emails.add(req.getEmail());
            arquivo.setCompartilhadoCom(emails);
            return toDTO(arquivoRepository.save(arquivo));
        }else{
            return toDTO(arquivo);
        }

	}
    public ArquivoDTO renomear(RenomearRequest req, Authentication authentication) throws Exception{
        Arquivo arquivo = pegarArquivoPorId(req.getId(), usuarioService.findUsuario(authentication));
        arquivo.setNome(req.getNome());
        return toDTO(arquivoRepository.save(arquivo));
    }
}
