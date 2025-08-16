package app.quadras.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<Usuario> usuariosCadastrados;

    @ManyToMany
    private List<Time> timesCadastrados;

    @ManyToOne
    private Horario horario;

    @ManyToOne
    private Quadra quadra;

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Usuario> getUsuariosCadastrados() {
        return usuariosCadastrados;
    }

    public void setUsuariosCadastrados(List<Usuario> usuariosCadastrados) {
        this.usuariosCadastrados = usuariosCadastrados;
    }

    public List<Time> getTimesCadastrados() {
        return timesCadastrados;
    }

    public void setTimesCadastrados(List<Time> timesCadastrados) {
        this.timesCadastrados = timesCadastrados;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Quadra getQuadra() {
        return quadra;
    }

    public void setQuadra(Quadra quadra) {
        this.quadra = quadra;
    }
}
