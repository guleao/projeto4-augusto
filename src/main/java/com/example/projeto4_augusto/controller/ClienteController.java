package com.example.projeto4_augusto.controller;

import com.example.projeto4_augusto.entity.Cliente;
import com.example.projeto4_augusto.entity.Endereco;
import com.example.projeto4_augusto.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {
        if (cliente.getEnderecos() != null) {
            cliente.getEnderecos().forEach(endereco -> endereco.setCliente(cliente));
        }
        Cliente novoCliente = clienteRepository.save(cliente);
        return ResponseEntity.status(201).body(novoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setNome(clienteDetails.getNome());
            cliente.setCpf(clienteDetails.getCpf());
            cliente.setEmail(clienteDetails.getEmail());

            cliente.getEnderecos().clear();
            if (clienteDetails.getEnderecos() != null) {
                clienteDetails.getEnderecos().forEach(endereco -> endereco.setCliente(cliente));
                cliente.getEnderecos().addAll(clienteDetails.getEnderecos());
            }

            Cliente clienteAtualizado = clienteRepository.save(cliente);
            return ResponseEntity.ok(clienteAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            return ResponseEntity.ok(clienteOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            clienteRepository.delete(clienteOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/enderecos")
    public ResponseEntity<Cliente> adicionarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.addEndereco(endereco);
            clienteRepository.save(cliente);
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/enderecos")
    public ResponseEntity<List<Endereco>> getEnderecos(@PathVariable Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            List<Endereco> enderecos = clienteOpt.get().getEnderecos();
            return ResponseEntity.ok(enderecos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}