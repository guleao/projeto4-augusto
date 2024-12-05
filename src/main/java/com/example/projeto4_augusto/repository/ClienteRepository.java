package com.example.projeto4_augusto.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.projeto4_augusto.entity.Cliente;
import com.example.projeto4_augusto.entity.Endereco;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByEnderecosContains(Endereco endereco);

}