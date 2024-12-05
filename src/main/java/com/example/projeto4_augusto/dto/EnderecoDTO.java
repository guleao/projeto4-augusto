package com.example.projeto4_augusto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoDTO {
    private String logradouro;
    private String cidade;
    private String estado;
    private Long clienteId;
    private Long colaboradorId;
}