package com.example.projeto4_augusto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projeto4_augusto.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}