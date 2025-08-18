package app.quadras.service;

import app.quadras.entity.Time;
import app.quadras.entity.Usuario;
import app.quadras.repository.TimeRepository;
import app.quadras.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TimeRepository timeRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario update = findById(id);
        if (usuario.getNome() != null && !usuario.getNome().isBlank()) {
            update.setNome(usuario.getNome());
        }
        if(usuario.getEmail()!=null&&!usuario.getEmail().isBlank()){
            update.setEmail(usuario.getEmail());
        }
        return usuarioRepository.save(update);
    }

    @Transactional
    public Usuario adicionarTimesProprietarios(Long idTime, Long idUsuario){
        Usuario proprietario = usuarioRepository.findById(idUsuario).orElseThrow(EntityNotFoundException::new);
        Time time = timeRepository.findById(idTime).orElseThrow(EntityNotFoundException::new);
        proprietario.getTimesProprietarios().add(time);
        time.setPresidente(proprietario);
        return proprietario;
    }

    @Transactional
    public Usuario adicionarTimesJogador(Long idTime, Long idUsuario){
        Usuario jogador = usuarioRepository.findById(idUsuario).orElseThrow(EntityNotFoundException::new);
        Time time = timeRepository.findById(idTime).orElseThrow(EntityNotFoundException::new);
        jogador.getTimes().add(time);
        time.getJogadores().add(jogador);
        return jogador;
    }

    @Transactional
    public Usuario removerTimesProprietarios(Long idUsuario, Long idTime){
        Usuario proprietario = usuarioRepository.findById(idUsuario).orElseThrow(EntityNotFoundException::new);
        Time time = timeRepository.findById(idTime).orElseThrow(EntityNotFoundException::new);
        proprietario.getTimesProprietarios().remove(time);
        time.setPresidente(null);
        return proprietario;
    }

    @Transactional
    public Usuario removerTimesJogador(Long idUsuario, Long idTime){
        Usuario jogador = usuarioRepository.findById(idUsuario).orElseThrow(EntityNotFoundException::new);
        Time time = timeRepository.findById(idTime).orElseThrow(EntityNotFoundException::new);
        jogador.getTimes().remove(time);
        time.getJogadores().remove(jogador);
        return jogador;
    }
}