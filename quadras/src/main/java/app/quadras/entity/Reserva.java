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

    public Reserva() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;


//    @OneToOne(mappedBy = "reserva", cascade = CascadeType.PERSIST)
//    @JsonIgnoreProperties("reserva")
//    private HorarioDia horario;

//    @ManyToMany(cascade = CascadeType.PERSIST)
//    private List<Time> timesCadastrados;

//    @ManyToMany(cascade = CascadeType.PERSIST)
//    private List<Usuario> usuariosCadastrados;

    @ManyToOne
    private Quadra quadra;
}
