package app.quadras.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime inicioReserva;

    // 💥 MUDANÇA: A reserva aponta para o CAMPO específico, não mais para a Quadra
    @ManyToOne
    @JoinColumn(name = "campo_id", nullable = false)
    private Campo campo;

    // Quem fez a reserva
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Reserva() {}
}