package app.quadras.service;

import app.quadras.entity.Horario;
import app.quadras.repository.HorarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Transactional(readOnly = true)
    public List<Horario> findAll() {
        return horarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Horario findById(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado com o id: " + id));
    }

    @Transactional
    public Horario save(Horario horario) {
        return horarioRepository.save(horario);
    }

    @Transactional
    public Horario update(Long id, Horario horarioDetails) {
        Horario horario = findById(id);
        horario.setHorario(horarioDetails.getHorario());
        horario.setData(horarioDetails.getData());
        horario.setQuadra(horarioDetails.getQuadra());
        horario.setTimesCadastrados(horarioDetails.getTimesCadastrados());
        horario.setUsuariosCadastrados(horarioDetails.getUsuariosCadastrados());
        horario.setReserva(horarioDetails.getReserva());
        return horarioRepository.save(horario);
    }

    @Transactional
    public void delete(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Horário não encontrado com o id: " + id);
        }
        horarioRepository.deleteById(id);
    }
}
