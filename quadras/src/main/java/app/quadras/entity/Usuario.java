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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @ManyToMany(mappedBy = "usuariosCadastrados")
    @JsonIgnoreProperties("usuariosCadastrados")
    private List<Horario> horarios;

    @OneToMany(mappedBy = "presidente")
    @JsonIgnoreProperties({"proprietario", "presidente", "jogadores"})
    private List<Time> timesProprietarios;

    @ManyToMany
    @JoinTable(name = "times_jogador")
    @JsonIgnoreProperties({"proprietario", "presidente", "jogadores"})
    private List<Time> times;

    @ManyToMany
    @JoinTable(name = "usuario_reservas")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "proprietario")
    @JsonIgnoreProperties({"proprietario", "presidente", "jogadores"})
    private List<Quadra> quadras;
}
