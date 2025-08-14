package app.quadras.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

//    @OneToMany(mappedBy = "presidente", cascade = CascadeType.ALL)
//    private List<Time> times;

//    @ManyToMany
//    @JoinTable(name = "horario_usuario")
//    private List<Horario> horariosCadastrados;


//    @ManyToMany
//    @JoinTable(name = "usuario_reservas")
//    private List<Reserva> reservas;

//    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL)
//    private List<Quadra> quadras;
}
