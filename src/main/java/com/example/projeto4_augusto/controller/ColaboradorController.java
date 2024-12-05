package com.example.projeto4_augusto.controller;

import com.example.projeto4_augusto.entity.Colaborador;
import com.example.projeto4_augusto.entity.Endereco;
import com.example.projeto4_augusto.repository.ColaboradorRepository;
import com.example.projeto4_augusto.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Colaborador> criarColaborador(@RequestBody Colaborador colaborador) {
        if (colaborador.getEnderecos() != null) {
            colaborador.getEnderecos().forEach(endereco -> endereco.setColaborador(colaborador));
        }
        Colaborador novoColaborador = colaboradorRepository.save(colaborador);
        return ResponseEntity.status(201).body(novoColaborador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Colaborador> atualizarColaborador(@PathVariable Long id,
            @RequestBody Colaborador colaboradorDetails) {
        Optional<Colaborador> colaboradorOpt = colaboradorRepository.findById(id);
        if (colaboradorOpt.isPresent()) {
            Colaborador colaborador = colaboradorOpt.get();
            colaborador.setNome(colaboradorDetails.getNome());
            colaborador.setCpf(colaboradorDetails.getCpf());
            colaborador.setEmail(colaboradorDetails.getEmail());
            colaborador.setCargo(colaboradorDetails.getCargo());

            colaborador.getEnderecos().clear();
            if (colaboradorDetails.getEnderecos() != null) {
                colaboradorDetails.getEnderecos().forEach(endereco -> endereco.setColaborador(colaborador));
                colaborador.getEnderecos().addAll(colaboradorDetails.getEnderecos());
            }

            Colaborador colaboradorAtualizado = colaboradorRepository.save(colaborador);
            return ResponseEntity.ok(colaboradorAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Colaborador>> getAllColaboradores() {
        List<Colaborador> colaboradores = colaboradorRepository.findAll();
        return ResponseEntity.ok(colaboradores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Colaborador> getColaboradorById(@PathVariable Long id) {
        Optional<Colaborador> colaboradorOpt = colaboradorRepository.findById(id);
        if (colaboradorOpt.isPresent()) {
            return ResponseEntity.ok(colaboradorOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarColaborador(@PathVariable Long id) {
        Optional<Colaborador> colaboradorOpt = colaboradorRepository.findById(id);
        if (colaboradorOpt.isPresent()) {
            colaboradorRepository.delete(colaboradorOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/enderecos")
    public ResponseEntity<Colaborador> adicionarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        Optional<Colaborador> colaboradorOpt = colaboradorRepository.findById(id);
        if (colaboradorOpt.isPresent()) {
            Colaborador colaborador = colaboradorOpt.get();
            colaborador.addEndereco(endereco);
            colaboradorRepository.save(colaborador);
            return ResponseEntity.ok(colaborador);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/enderecos")
    public ResponseEntity<List<Endereco>> getEnderecos(@PathVariable Long id) {
        Optional<Colaborador> colaboradorOpt = colaboradorRepository.findById(id);
        if (colaboradorOpt.isPresent()) {
            List<Endereco> enderecos = colaboradorOpt.get().getEnderecos();
            return ResponseEntity.ok(enderecos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}