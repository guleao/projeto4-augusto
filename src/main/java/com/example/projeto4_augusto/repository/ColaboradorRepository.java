package com.example.projeto4_augusto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projeto4_augusto.entity.Colaborador;
import com.example.projeto4_augusto.entity.Endereco;
import java.util.List;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    List<Colaborador> findByEnderecosContains(Endereco endereco);
}