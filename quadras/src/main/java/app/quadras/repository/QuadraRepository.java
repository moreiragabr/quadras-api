package app.quadras.repository;

import app.quadras.entity.Quadra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuadraRepository extends JpaRepository<Quadra, Long> {}