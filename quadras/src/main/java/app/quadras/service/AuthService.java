package app.quadras.service;

import app.quadras.dto.LoginResponseDTO;
import app.quadras.dto.RegistroResponseDTO;
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


    public Optional<LoginResponseDTO> loginAndGenerateToken(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();


            if (passwordEncoder.matches(senha, usuario.getSenha())) {

                String token = tokenService.generateToken(usuario);

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

    public RegistroResponseDTO registrar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Usuario usuarioSalvo =usuarioRepository.save(usuario);

        RegistroResponseDTO responseDTO = new RegistroResponseDTO(
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

        return responseDTO;
    }
}