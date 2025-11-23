package app.quadras.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class HorarioDia {

    public HorarioDia() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dayOfWeek;

    private String dayName;

    private Boolean isOpen;

    private String openTime;

    private String closeTime;

    @ManyToOne
    @JsonIgnoreProperties("horariosDeFuncionamento")
    private Quadra quadra;
}
