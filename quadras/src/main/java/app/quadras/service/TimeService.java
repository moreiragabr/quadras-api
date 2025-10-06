package app.quadras.service;

import app.quadras.entity.Time;
import app.quadras.entity.Usuario;
import app.quadras.repository.TimeRepository;
import app.quadras.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository timeRepository;

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public Time findById(Long id) {
        return timeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Time save(Time time) {
        return timeRepository.save(time);
    }

    @Transactional
    public void delete(Long id) {
        Time time = findById(id);
        if (time.getJogadores() != null) {
            time.getJogadores().clear();
        }
        timeRepository.saveAndFlush(time);
        timeRepository.delete(time);
    }

    public Time update(Long id, Time time) {
        Time update = findById(id);
        if (time.getNome() != null && !time.getNome().isBlank()) {
            update.setNome(time.getNome());
        }
        if(time.getTipoEsporte()!=null){
            update.setTipoEsporte(time.getTipoEsporte());
        }
        return timeRepository.save(update);
    }
}
