package app.quadras.service;

import app.quadras.dto.PerfilResponseDTO;
import app.quadras.entity.Quadra;
//import app.quadras.entity.Time;
import app.quadras.entity.TipoUsuario;
import app.quadras.entity.Usuario;
import app.quadras.repository.QuadraRepository;
//import app.quadras.repository.TimeRepository;
import app.quadras.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    //    private final TimeRepository timeRepository;
    private final QuadraRepository quadraRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public PerfilResponseDTO findById(Long id) {
        return usuarioRepository.findById(id)
                .map(PerfilResponseDTO::fromUsuario)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuário não encontrado com ID: " + id));
    }

    public Usuario findEntityById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuário não encontrado com ID: " + id));
    }

    public Optional<Usuario> findByEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario;
    }

    public Usuario save(Usuario usuario) {
        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(TipoUsuario.USER);
            return usuarioRepository.save(usuario);
        }
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        Usuario usuario = findEntityById(id);
        usuarioRepository.delete(usuario);
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario update = findEntityById(id);

        if (usuario.getNome() != null && !usuario.getNome().isBlank()) {
            update.setNome(usuario.getNome());
        }
        if (usuario.getEmail() != null && !usuario.getEmail().isBlank()) {
            update.setEmail(usuario.getEmail());
        }

        return usuarioRepository.save(update);
    }

//    @Transactional
//    public Usuario adicionarTimesProprietarios(Long idTime, Long idUsuario) {
//        Usuario proprietario = usuarioRepository.findById(idUsuario).orElseThrow(EntityNotFoundException::new);
//        Time time = timeRepository.findById(idTime).orElseThrow(EntityNotFoundException::new);
//        proprietario.getTimesProprietarios().add(time);
//        time.setPresidente(proprietario);
//        return proprietario;
//    }

//    @Transactional
//    public Usuario adicionarTimesJogador(Long idTime, Long idUsuario) {
//        Usuario jogador = usuarioRepository.findById(idUsuario).orElseThrow(EntityNotFoundException::new);
//        Time time = timeRepository.findById(idTime).orElseThrow(EntityNotFoundException::new);
//        jogador.getTimes().add(time);
//        time.getJogadores().add(jogador);
//        return jogador;
//    }

//    @Transactional
//    public Usuario removerTimesProprietarios(Long idUsuario, Long idTime) {
//        Usuario proprietario = usuarioRepository.findById(idUsuario).orElseThrow(EntityNotFoundException::new);
//        Time time = timeRepository.findById(idTime).orElseThrow(EntityNotFoundException::new);
//        proprietario.getTimesProprietarios().remove(time);
//        time.setPresidente(null);
//        return proprietario;
//    }
//
//    @Transactional
//    public Usuario removerTimesJogador(Long idUsuario, Long idTime) {
//        Usuario jogador = usuarioRepository.findById(idUsuario).orElseThrow(EntityNotFoundException::new);
//        Time time = timeRepository.findById(idTime).orElseThrow(EntityNotFoundException::new);
//        jogador.getTimes().remove(time);
//        time.getJogadores().remove(jogador);
//        return jogador;
//    }

    // --------------------------------------------
    // ADICIONAR QUADRA AO PROPRIETARIO
    // --------------------------------------------
    @Transactional
    public Usuario adicionarQuadraProprietario(Long idUsuario, Long idQuadra) {
        Usuario proprietario = findEntityById(idUsuario);
        Quadra quadra = quadraRepository.findById(idQuadra).orElseThrow(EntityNotFoundException::new);

        proprietario.getQuadras().add(quadra);
        quadra.setProprietario(proprietario);

        return proprietario;
    }
}
