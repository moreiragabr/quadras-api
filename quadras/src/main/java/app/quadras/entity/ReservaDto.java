package app.quadras.entity;

import java.util.List;

public class ReservaDto {
    private List<Long> timesIds;
    private List<Long> usuariosIds;
    private Long horarioId;
    private Long quadraId;

    public List<Long> getTimesIds() { return timesIds; }
    public void setTimesIds(List<Long> timesIds) { this.timesIds = timesIds; }
    public List<Long> getUsuariosIds() { return usuariosIds; }
    public void setUsuariosIds(List<Long> usuariosIds) { this.usuariosIds = usuariosIds; }
    public Long getHorarioId() { return horarioId; }
    public void setHorarioId(Long horarioId) { this.horarioId = horarioId; }
    public Long getQuadraId() { return quadraId; }
    public void setQuadraId(Long quadraId) { this.quadraId = quadraId; }
}
