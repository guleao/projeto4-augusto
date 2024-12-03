package com.example.projeto4_augusto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projeto4_augusto.entity.Colaborador;
import com.example.projeto4_augusto.repository.ColaboradorRepository;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @GetMapping
    public ResponseEntity<List<Colaborador>> getAllColaboradores() {
        List<Colaborador> colaboradores = colaboradorRepository.findAll();
        return new ResponseEntity<>(colaboradores, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Colaborador> getColaboradorById(@PathVariable Long id) {
        return colaboradorRepository.findById(id)
                .map(colaborador -> new ResponseEntity<>(colaborador, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Colaborador> criarColaborador(@RequestBody Colaborador colaborador) {
        Colaborador novoColaborador = colaboradorRepository.save(colaborador);
        return new ResponseEntity<>(novoColaborador, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Colaborador> atualizarColaborador(@PathVariable Long id,
            @RequestBody Colaborador colaboradorDetails) {
        return colaboradorRepository.findById(id)
                .map(colaborador -> {
                    colaborador.setNome(colaboradorDetails.getNome());
                    colaborador.setCpf(colaboradorDetails.getCpf());
                    colaborador.setEmail(colaboradorDetails.getEmail());
                    colaborador.setCargo(colaboradorDetails.getCargo());
                    colaborador.setEndereco(colaboradorDetails.getEndereco());
                    Colaborador colaboradorAtualizado = colaboradorRepository.save(colaborador);
                    return new ResponseEntity<>(colaboradorAtualizado, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarColaborador(@PathVariable Long id) {
        return colaboradorRepository.findById(id)
                .map(colaborador -> {
                    colaboradorRepository.delete(colaborador);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}