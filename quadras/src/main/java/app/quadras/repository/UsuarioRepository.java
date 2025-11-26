package app.quadras.repository;

import app.quadras.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u " +
            "LEFT JOIN FETCH u.reservas r " +
            "LEFT JOIN FETCH r.campo c " +
            "LEFT JOIN FETCH c.quadra q " +
            "WHERE u.id = :id")
    Optional<Usuario> findByIdWithFullReservas(Long id);
}
