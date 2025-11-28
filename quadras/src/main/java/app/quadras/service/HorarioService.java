package app.quadras.service;

import app.quadras.entity.HorarioDia;
import app.quadras.repository.HorarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioService {

    private final HorarioRepository repository;

    public List<HorarioDia> findAll() {
        return repository.findAll();
    }

    public HorarioDia findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado com id " + id));
    }

    public HorarioDia save(HorarioDia horarioDia) {
        if (horarioDia.getQuadra() == null) {
            throw new RuntimeException("Horário precisa de uma quadra");
        }
        return repository.save(horarioDia);
    }

    public void delete(Long id) {
        HorarioDia existente = findById(id);
        repository.delete(existente);
    }
//
//    public HorarioDia update(Long id, HorarioDia horario) {
//        HorarioDia existente = findById(id);
//
//        if (horario.getHorario() != null && !horario.getHorario().isBlank()) {
//            existente.setHorario(horario.getHorario());
//        }
//        if (horario.getData() != null && !horario.getData().isBlank()) {
//            existente.setData(horario.getData());
//        }
//        if (horario.getQuadra() != null) {
//            existente.setQuadra(horario.getQuadra());
//        }
//        if (horario.getTimesCadastrados() != null) {
//            existente.setTimesCadastrados(horario.getTimesCadastrados());
//        }
//        if (horario.getUsuariosCadastrados() != null) {
//            existente.setUsuariosCadastrados(horario.getUsuariosCadastrados());
//        }
//        if (horario.getReserva() != null) {
//            existente.setReserva(horario.getReserva());
//        }
//
//        return repository.save(existente);
//    }
}