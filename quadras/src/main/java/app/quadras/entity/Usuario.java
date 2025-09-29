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

    @OneToMany(mappedBy = "presidente", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"presidente", "jogadores"})
    private List<Time> timesProprietarios;

    @ManyToMany(mappedBy = "jogadores")
    @JsonIgnoreProperties({"presidente", "jogadores"})
    private List<Time> times;

    @ManyToMany()
    @JoinTable(name = "usuario_reservas")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"proprietario", "presidente", "jogadores"})
    private List<Quadra> quadras;
}
