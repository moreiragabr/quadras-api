package app.quadras.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
public class Quadra {

    public Quadra() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(length = 500)
    private String descricao;

//    private String nota;
    private Float valorHora;
    private Boolean partidaGravavel;
    private String cidade;
    private String bairro;
    private String estado;

    @Enumerated(EnumType.STRING)
    private TipoEsporte tipoQuadra;

    @OneToMany(mappedBy = "quadra", cascade = CascadeType.REMOVE)
    private List<Horario> horarios;

    @ManyToOne
    @JsonIgnoreProperties("quadras")
    private Usuario proprietario;
}