package app.quadras.repository;

import app.quadras.entity.Campo; // Importa a entidade Campo
import app.quadras.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Busca todas as reservas para um CAMPO em uma data específica.
     * @param campo A entidade Campo.
     * @param data A data (LocalDate) da reserva.
     * @return Lista de Reservas.
     */
    @Query("SELECT r FROM Reserva r WHERE r.campo = :campo AND DATE(r.inicioReserva) = :data")
    List<Reserva> findByCampoAndData(@Param("campo") Campo campo, @Param("data") LocalDate data);


    /**
     * Verifica se já existe uma reserva para o slot exato (campo e horário de início).
     * @param campo O campo.
     * @param inicioReserva O horário exato de início da reserva (LocalDateTime).
     * @return Optional<Reserva> se encontrada.
     */
    Optional<Reserva> findByCampoAndInicioReserva(Campo campo, LocalDateTime inicioReserva);
}