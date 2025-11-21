package app.quadras.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class HorarioDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dayOfWeek;
    private String dayName;
    private Boolean isOpen;
    private String openTime;
    private String closeTime;

    @ManyToOne
    @NotNull
    @JsonIgnoreProperties("horariosDeFuncionamento")
    private Quadra quadra;
}
