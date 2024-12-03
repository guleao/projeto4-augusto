package com.example.projeto4_augusto.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Cliente cliente;
    private Colaborador colaborador;
    private String descritivo;
    private String situacao;
    private String dataAbertura;
    private String dataFechamento;

}
