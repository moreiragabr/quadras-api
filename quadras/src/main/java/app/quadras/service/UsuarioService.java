package app.quadras.service;

import app.quadras.entity.Usuario;
import app.quadras.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

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
}
