package com.example.projeto4_augusto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logradouro;
    private String cidade;
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "colaborador_id")
    @JsonIgnore
    private Colaborador colaborador;

    @JsonProperty("clienteId")
    public Long getClienteId() {
        return (cliente != null) ? cliente.getId() : null;
    }

    @JsonProperty("colaboradorId")
    public Long getColaboradorId() {
        return (colaborador != null) ? colaborador.getId() : null;
    }
}