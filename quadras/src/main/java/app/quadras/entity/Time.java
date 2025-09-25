package app.quadras.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany(mappedBy = "timesCadastrados")
    @JsonIgnoreProperties("timesCadastrados")
    private List<Horario> horarios;

    @Enumerated(EnumType.STRING)
    private TipoEsporte tipoEsporte;

    @ManyToOne()
    @JsonIgnoreProperties({"times", "timesProprietarios"})
    private Usuario presidente;

    @ManyToMany()
    @JoinTable(name = "times_jogador")
    @JsonIgnoreProperties({"times", "timesProprietarios"})
    private List<Usuario> jogadores;

    @ManyToMany()
    @JoinTable(name = "time_reserva")
    private List<Reserva> reservas;
}
