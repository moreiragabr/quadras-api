package app.quadras.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Quadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String nota;
    private float valorHora;
    private boolean partidaGravavel;
    private String localizacao;

    @Enumerated(EnumType.STRING)
    private TipoEsporte tipoQuadra;

    @OneToMany(mappedBy = "quadra")
    private List<Horario> horarios;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
    public float getValorHora() { return valorHora; }
    public void setValorHora(float valorHora) { this.valorHora = valorHora; }
    public boolean isPartidaGravavel() { return partidaGravavel; }
    public void setPartidaGravavel(boolean partidaGravavel) { this.partidaGravavel = partidaGravavel; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public TipoEsporte getTipoQuadra() { return tipoQuadra; }
    public void setTipoQuadra(TipoEsporte tipoQuadra) { this.tipoQuadra = tipoQuadra; }
    public List<Horario> getHorarios() { return horarios; }
    public void setHorarios(List<Horario> horarios) { this.horarios = horarios; }
}
