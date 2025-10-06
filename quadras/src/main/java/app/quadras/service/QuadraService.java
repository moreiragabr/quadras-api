package app.quadras.service;

import app.quadras.entity.Quadra;
import app.quadras.repository.QuadraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuadraService {

    @Autowired
    private QuadraRepository quadraRepository;

    @Transactional(readOnly = true)
    public List<Quadra> findAll() {
        return quadraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Quadra findById(Long id) {
        return quadraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quadra não encontrada com o id: " + id));
    }

    @Transactional
    public Quadra save(Quadra quadra) {
        return quadraRepository.save(quadra);
    }

    @Transactional
    public Quadra update(Long id, Quadra quadraDetails) {
        Quadra quadra = findById(id);
        quadra.setNome(quadraDetails.getNome());
//        quadra.setNota(quadraDetails.getNota());
        quadra.setValorHora(quadraDetails.getValorHora());
        quadra.setPartidaGravavel(quadraDetails.isPartidaGravavel());
        quadra.setLocalizacao(quadraDetails.getLocalizacao());
        quadra.setTipoQuadra(quadraDetails.getTipoQuadra());
        return quadraRepository.save(quadra);
    }

    @Transactional
    public void delete(Long id) {
        if (!quadraRepository.existsById(id)) {
            throw new EntityNotFoundException("Quadra não encontrada com o id: " + id);
        }
        quadraRepository.deleteById(id);
    }
}