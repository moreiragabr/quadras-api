package app.quadras.entity;

import jakarta.persistence.*;

@Entity
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String horario;
    private String data;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}
