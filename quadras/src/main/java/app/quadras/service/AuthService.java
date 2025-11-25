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
    private final TokenService tokenService; // Injetando TokenService para orquestração

    /**
     * Tenta autenticar o usuário e, se bem-sucedido, retorna o DTO com o Token.
     * * @param email O email do usuário.
     * @param senha A senha em texto puro.
     * @return Um Optional contendo o DTO de resposta (token e dados do usuário) ou Optional.empty().
     */
    public Optional<LoginResponseDTO> loginAndGenerateToken(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // 1. Lógica de validação de senha (regra de negócio principal)
            if (passwordEncoder.matches(senha, usuario.getSenha())) {

                // 2. Geração do Token (orquestração de outro serviço)
                String token = tokenService.generateToken(usuario);

                // 3. Lógica de formatação da Role para o DTO (preparação da resposta)
                String role = usuario.getAuthorities().stream()
                        .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                        .findFirst()
                        .orElse("USER");

                // 4. Montagem do DTO de resposta
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

        // Retorna Optional vazio se a autenticação falhar
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