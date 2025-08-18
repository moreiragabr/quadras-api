package app.quadras.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @OneToOne(mappedBy = "reserva")
    @JsonIgnoreProperties("reserva")
    private Horario horario;

    @ManyToMany
    private List<Time> timesCadastrados;

    @ManyToMany
    private List<Usuario> usuariosCadastrados;

    @ManyToOne
    private Quadra quadra;

//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    public List<Time> getTimesCadastrados() { return timesCadastrados; }
//    public void setTimesCadastrados(List<Time> timesCadastrados) { this.timesCadastrados = timesCadastrados; }
//    public List<Usuario> getUsuariosCadastrados() { return usuariosCadastrados; }
//    public void setUsuariosCadastrados(List<Usuario> usuariosCadastrados) { this.usuariosCadastrados = usuariosCadastrados; }
//    public Horario getHorario() { return horario; }
//    public void setHorario(Horario horario) { this.horario = horario; }
//    public Quadra getQuadra() { return quadra; }
//    public void setQuadra(Quadra quadra) { this.quadra = quadra; }
}
