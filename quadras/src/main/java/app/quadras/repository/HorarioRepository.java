package app.quadras.repository;

import app.quadras.entity.HorarioDia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioRepository extends JpaRepository<HorarioDia, Long> {}
