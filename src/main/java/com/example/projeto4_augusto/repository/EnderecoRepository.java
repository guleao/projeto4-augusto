package com.example.projeto4_augusto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projeto4_augusto.entity.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}