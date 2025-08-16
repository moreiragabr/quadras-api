package app.quadras.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String horario;

    @NotBlank
    private String data;

    @ManyToOne
    @NotNull
    @JsonIgnoreProperties("horarios")
    private Quadra quadra;

    @ManyToMany
    @JoinTable(
            name = "horario_times",
            joinColumns = @JoinColumn(name = "horario_id"),
            inverseJoinColumns = @JoinColumn(name = "time_id")
    )
    @JsonIgnoreProperties("horarios")
    private List<Time> timesCadastrados;

    @ManyToMany
    @JoinTable(
            name = "horario_usuarios",
            joinColumns = @JoinColumn(name = "horario_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties("horarios")
    private List<Usuario> usuariosCadastrados;

    @OneToOne
    @JoinColumn(name = "reserva_id")
    @JsonIgnoreProperties("horario")
    private Reserva reserva;

    // Getters e Setters manuais
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public Quadra getQuadra() { return quadra; }
    public void setQuadra(Quadra quadra) { this.quadra = quadra; }

    public List<Time> getTimesCadastrados() { return timesCadastrados; }
    public void setTimesCadastrados(List<Time> timesCadastrados) { this.timesCadastrados = timesCadastrados; }

    public List<Usuario> getUsuariosCadastrados() { return usuariosCadastrados; }
    public void setUsuariosCadastrados(List<Usuario> usuariosCadastrados) { this.usuariosCadastrados = usuariosCadastrados; }

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }
}
