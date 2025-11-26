package app.quadras.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne
    @JoinColumn(name = "campo_id", nullable = false)
    @JsonIgnoreProperties("quadra")
    private Campo campo;

    @ManyToOne
    @JsonBackReference("usuario-reserva") // Deve corresponder ao valor em Usuario.reservas
    private Usuario usuario;

    public Reserva() {}
}