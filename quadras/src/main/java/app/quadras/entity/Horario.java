package app.quadras.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Horario {

    public Horario() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dayOfWeek;

    private String dayName;

    private Boolean isOpen;

    private String openTime;

    private String closeTime;

    @ManyToOne
    @JsonIgnore
    private Quadra quadra;
}
