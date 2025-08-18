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
}
