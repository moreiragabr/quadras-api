package app.quadras.service;

import app.quadras.entity.Time;
import app.quadras.repository.TimeRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public void delete(Long id) {
        Time time = findById(id);
        timeRepository.delete(time);
    }

    public Time update(Long id, Time time) {
        Time update = findById(id);
        if (time.getNome() != null && !time.getNome().isBlank()) {
            update.setNome(time.getNome());
        }
        if (time.getPresidente()!=null){
            update.setPresidente(time.getPresidente());
        }
        if(time.getJogadores()!=null){
            update.setJogadores(time.getJogadores());
        }
        return timeRepository.save(update);
    }
}
