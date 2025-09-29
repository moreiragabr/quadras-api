package app.quadras.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Quadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
//    private String nota;
    private float valorHora;
    private boolean partidaGravavel;
    private String localizacao;

    @Enumerated(EnumType.STRING)
    private TipoEsporte tipoQuadra;

    @OneToMany(mappedBy = "quadra", cascade = CascadeType.REMOVE)
    private List<Horario> horarios;

    @ManyToOne
    @JsonIgnoreProperties("quadras")
    private Usuario proprietario;
}