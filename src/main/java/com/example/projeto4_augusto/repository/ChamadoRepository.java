package com.example.projeto4_augusto.repository;

import com.example.projeto4_augusto.entity.Chamado;
import com.example.projeto4_augusto.entity.Situacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findByClienteId(Long clienteId);

    List<Chamado> findByColaboradorId(Long colaboradorId);

    @Query("SELECT COUNT(c) FROM Chamado c WHERE c.situacao = :situacao AND MONTH(c.dataEncerramento) = :mes AND YEAR(c.dataEncerramento) = :ano")
    Long countBySituacaoAndMonthAndYear(Situacao situacao, int mes, int ano);

    @Query("SELECT c.situacao, COUNT(c) FROM Chamado c WHERE YEAR(c.dataAbertura) = :ano GROUP BY c.situacao")
    List<Object[]> countBySituacaoGroupedByYear(int ano);

    @Query("SELECT " +
            "SUM(CASE WHEN c.situacao = 'ATENDIDO' THEN 1 ELSE 0 END) / " +
            "SUM(CASE WHEN c.situacao IN ('ATENDIDO', 'ABERTO') THEN 1 ELSE 0 END) " +
            "FROM Chamado c")
    Double calcularCoeficienteAtendimento();
}