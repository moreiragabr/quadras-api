package app.quadras.service;

import app.quadras.dto.LoginResponseDTO;
import app.quadras.dto.RegistroResponseDTO;
import app.quadras.entity.TipoUsuario;
import app.quadras.entity.Usuario;
import app.quadras.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    // LOGIN
    public Optional<LoginResponseDTO> loginAndGenerateToken(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (passwordEncoder.matches(senha, usuario.getSenha())) {

                String token = tokenService.generateToken(usuario);

                // Pega a role do usuário
                String role = usuario.getAuthorities().stream()
                        .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                        .findFirst()
                        .orElse("USER");

                LoginResponseDTO responseDTO = new LoginResponseDTO(
                        token,
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        role
                );

                return Optional.of(responseDTO);
            }
        }

        return Optional.empty();
    }

    // REGISTRO
    public RegistroResponseDTO registrar(Usuario usuario) {

        // Normaliza o TipoUsuario
        if (usuario.getTipoUsuario() != null) {
            switch (usuario.getTipoUsuario().name().toUpperCase()) {
                case "ADMIN":
                    usuario.setTipoUsuario(TipoUsuario.ADMIN);
                    break;
                case "COMUM": // Caso algum JSON envie COMUM
                case "USER":
                default:
                    usuario.setTipoUsuario(TipoUsuario.USER);
            }
        } else {
            usuario.setTipoUsuario(TipoUsuario.USER); // default
        }

        // Criptografa senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        // Salva usuário no banco
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Retorna DTO
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
