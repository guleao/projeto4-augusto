package com.example.projeto4_augusto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String email;
    private String cargo;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Chamado> chamados = new ArrayList<>();

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Endereco> enderecos = new ArrayList<>();

    public void addChamado(Chamado chamado) {
        chamados.add(chamado);
        chamado.setColaborador(this);
    }

    public void removeChamado(Chamado chamado) {
        chamados.remove(chamado);
        chamado.setColaborador(null);
    }

    public void addEndereco(Endereco endereco) {
        enderecos.add(endereco);
        endereco.setColaborador(this);
    }

    public void removeEndereco(Endereco endereco) {
        enderecos.remove(endereco);
        endereco.setColaborador(null);
    }
}