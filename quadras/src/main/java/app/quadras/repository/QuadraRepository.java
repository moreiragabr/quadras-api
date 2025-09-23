package app.quadras.repository;

import app.quadras.entity.Quadra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface QuadraRepository extends JpaRepository<Quadra, Long> {
}