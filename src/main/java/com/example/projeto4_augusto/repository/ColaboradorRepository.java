package com.example.projeto4_augusto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projeto4_augusto.entity.Colaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
}