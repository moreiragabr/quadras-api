package app.quadras.Service;

import app.quadras.Entity.Horario;
import app.quadras.Repository.HorarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HorarioService {

    private final HorarioRepository repository;

    public HorarioService(HorarioRepository repository) {
        this.repository = repository;
    }

    public List<Horario> findAll() {
        return repository.findAll();
    }

    public Horario findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado com id " + id));
    }

    public Horario save(Horario horario) {
        if (horario.getQuadra() == null) {
            throw new RuntimeException("Horário precisa de uma quadra");
        }
        return repository.save(horario);
    }

    public void delete(Long id) {
        Horario existente = findById(id);
        repository.delete(existente);
    }

    public Horario update(Long id, Horario horario) {
        Horario existente = findById(id);

        if (horario.getHorario() != null && !horario.getHorario().isBlank()) {
            existente.setHorario(horario.getHorario());
        }
        if (horario.getData() != null && !horario.getData().isBlank()) {
            existente.setData(horario.getData());
        }
        if (horario.getQuadra() != null) {
            existente.setQuadra(horario.getQuadra());
        }
        if (horario.getTimesCadastrados() != null) {
            existente.setTimesCadastrados(horario.getTimesCadastrados());
        }
        if (horario.getUsuariosCadastrados() != null) {
            existente.setUsuariosCadastrados(horario.getUsuariosCadastrados());
        }
        if (horario.getReserva() != null) {
            existente.setReserva(horario.getReserva());
        }

        return repository.save(existente);
    }
}
