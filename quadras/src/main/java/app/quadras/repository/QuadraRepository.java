package app.quadras.repository;

import app.quadras.entity.Campo;
import app.quadras.entity.Quadra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface QuadraRepository extends JpaRepository<Quadra, Long> {
    @Query("SELECT c FROM Campo c JOIN FETCH c.quadra q WHERE c.id = :id")
    Optional<Campo> findByIdWithQuadra(@Param("id") Long id);
}