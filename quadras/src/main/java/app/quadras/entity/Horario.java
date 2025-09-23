package app.quadras.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
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
}
