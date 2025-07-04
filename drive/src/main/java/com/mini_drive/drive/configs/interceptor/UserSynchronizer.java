package com.mini_drive.drive.configs.interceptor;

import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.mini_drive.drive.entities.usuario.Usuario;
import com.mini_drive.drive.entities.usuario.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizer {
    private final UsuarioRepository userRepository;
    private final UserMapper userMapper;
    public void synchronizeUser(Jwt token) {
    log.info("Sincronizando usuario");
    getEmail(token).ifPresent(email -> {
        String id = token.getClaim("sub");
        Usuario userFromToken = userMapper.toUsuario(token.getClaims());
        Usuario user;
        if (id != null && userRepository.existsById(id)) {
            user = userRepository.findById(id).get();
            user.setFirstName(userFromToken.getFirstName());
            user.setLastName(userFromToken.getLastName());
            user.setEmail(userFromToken.getEmail());
            user.setLastSeenAt(userFromToken.getLastSeenAt());
        } else {
            user = userFromToken;
        }
        log.info("{}", userRepository.save(user));
    });
}

    public Optional<String> getEmail(Jwt token) {
        return Optional.ofNullable(token.getClaim("email"));
    }


}
