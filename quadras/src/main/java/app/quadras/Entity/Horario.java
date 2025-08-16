package app.quadras.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String horario;

    private String data;

    @ManyToOne
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

    @OneToOne(mappedBy = "horario")
    @JsonIgnoreProperties("horario")
    private Reserva reserva;
}
