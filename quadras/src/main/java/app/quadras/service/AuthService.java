package app.quadras.service;

import app.quadras.dto.RegistroResponseDTO;
import app.quadras.entity.TipoUsuario;
import app.quadras.entity.Usuario;
import app.quadras.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegistroResponseDTO registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email ja cadastrado: " + usuario.getEmail());
        }

        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(TipoUsuario.SYSJEGG_USER);
        }

        // 1. Criar no Keycloak primeiro (ele precisa da senha em texto puro)
        keycloakAdminService.createUser(usuario);

        // 2. Salvar no Banco Local (a senha será ignorada pelo Hibernate devido ao @Column(insertable=false))
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new RegistroResponseDTO(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getTipoUsuario(),
                usuarioSalvo.getCidade(),
                usuarioSalvo.getBairro(),
                usuarioSalvo.getEstado(),
                usuarioSalvo.getRua(),
                usuarioSalvo.getNumeroCasa(),
                usuarioSalvo.getCep()
        );
    }
}
