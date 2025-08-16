package app.quadras.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Quadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String endereco;

    @OneToMany(mappedBy = "quadra")
    @JsonIgnoreProperties("quadra")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "quadra")
    @JsonIgnoreProperties("quadra")
    private List<Horario> horarios;
}
