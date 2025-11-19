package app.quadras.service;

import app.quadras.entity.Campo;
import app.quadras.repository.CampoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampoService {

    CampoRepository campoRepository;

    public Campo findById(Long id) {
        return campoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Campo save(Campo campo) {
        return campoRepository.save(campo);
    }

    public void delete(Long id) {
        Campo campo = findById(id);
        campoRepository.delete(campo);
    }

    public Campo update(Long id, Campo campo) {
        Campo update = findById(id);
        if (campo.getNome() != null && !campo.getNome().isBlank()) {
            update.setNome(campo.getNome());
        }
        return campoRepository.save(update);
    }
}
