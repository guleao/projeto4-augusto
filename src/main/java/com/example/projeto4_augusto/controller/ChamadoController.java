package com.example.projeto4_augusto.controller;

import com.example.projeto4_augusto.entity.Chamado;
import com.example.projeto4_augusto.entity.Colaborador;
import com.example.projeto4_augusto.entity.Cliente;
import com.example.projeto4_augusto.entity.Situacao;
import com.example.projeto4_augusto.repository.ChamadoRepository;
import com.example.projeto4_augusto.repository.ColaboradorRepository;
import com.example.projeto4_augusto.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Chamado> criarChamado(@RequestBody Chamado chamado) {

        if (chamado.getDescricao() == null || chamado.getDescricao().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (chamado.getSituacao() == null) {
            chamado.setSituacao(Situacao.EM_ESPERA);
        }

        if (chamado.getDataAbertura() == null) {
            chamado.setDataAbertura(LocalDateTime.now());
        }

        if (chamado.getCliente() == null || chamado.getCliente().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Cliente> clienteOpt = clienteRepository.findById(chamado.getCliente().getId());
        if (!clienteOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        chamado.setCliente(clienteOpt.get());

        if (chamado.getColaborador() != null && chamado.getColaborador().getId() != null) {
            Optional<Colaborador> colaboradorOpt = colaboradorRepository.findById(chamado.getColaborador().getId());
            if (!colaboradorOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            chamado.setColaborador(colaboradorOpt.get());
        } else {
            chamado.setColaborador(null);
        }

        Chamado novoChamado = chamadoRepository.save(chamado);
        return ResponseEntity.status(201).body(novoChamado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chamado> atualizarChamado(@PathVariable Long id, @RequestBody Chamado chamadoDetails) {
        Optional<Chamado> chamadoOpt = chamadoRepository.findById(id);
        if (chamadoOpt.isPresent()) {
            Chamado chamado = chamadoOpt.get();
            chamado.setDescricao(chamadoDetails.getDescricao());
            chamado.setSituacao(chamadoDetails.getSituacao());
            chamado.setDataEncerramento(chamadoDetails.getDataEncerramento());

            if (chamadoDetails.getCliente() != null && chamadoDetails.getCliente().getId() != null) {
                Optional<Cliente> clienteOpt = clienteRepository.findById(chamadoDetails.getCliente().getId());
                if (clienteOpt.isPresent()) {
                    chamado.setCliente(clienteOpt.get());
                } else {
                    chamado.setCliente(null);
                }
            } else {
                chamado.setCliente(null);
            }

            if (chamadoDetails.getColaborador() != null && chamadoDetails.getColaborador().getId() != null) {
                Optional<Colaborador> colaboradorOpt = colaboradorRepository
                        .findById(chamadoDetails.getColaborador().getId());
                if (colaboradorOpt.isPresent()) {
                    chamado.setColaborador(colaboradorOpt.get());
                } else {
                    chamado.setColaborador(null);
                }
            } else {
                chamado.setColaborador(null);
            }

            Chamado chamadoAtualizado = chamadoRepository.save(chamado);
            return ResponseEntity.ok(chamadoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Chamado>> getAllChamados() {
        List<Chamado> chamados = chamadoRepository.findAll();
        return ResponseEntity.ok(chamados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamado> getChamadoById(@PathVariable Long id) {
        Optional<Chamado> chamadoOpt = chamadoRepository.findById(id);
        if (chamadoOpt.isPresent()) {
            return ResponseEntity.ok(chamadoOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletarChamado(@PathVariable Long id) {
        Optional<Chamado> chamadoOpt = chamadoRepository.findById(id);
        if (chamadoOpt.isPresent()) {
            chamadoRepository.delete(chamadoOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Chamado>> getChamadosByCliente(@PathVariable Long clienteId) {
        List<Chamado> chamados = chamadoRepository.findByClienteId(clienteId);
        return ResponseEntity.ok(chamados);
    }

    @GetMapping("/colaborador/{colaboradorId}")
    public ResponseEntity<List<Chamado>> getChamadosByColaborador(@PathVariable Long colaboradorId) {
        List<Chamado> chamados = chamadoRepository.findByColaboradorId(colaboradorId);
        return ResponseEntity.ok(chamados);
    }

    @PutMapping("/{id}/atender")
    public ResponseEntity<Chamado> atenderChamado(@PathVariable Long id) {
        Optional<Chamado> chamadoOpt = chamadoRepository.findById(id);
        if (chamadoOpt.isPresent()) {
            Chamado chamado = chamadoOpt.get();
            chamado.setSituacao(Situacao.ATENDIDO);
            chamado.setDataEncerramento(LocalDateTime.now());
            Chamado chamadoAtualizado = chamadoRepository.save(chamado);
            return ResponseEntity.ok(chamadoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estatisticas/atendidos")
    public ResponseEntity<Map<String, Long>> contarAtendidosNoMes(@RequestParam int mes, @RequestParam int ano) {
        Long totalAtendidos = chamadoRepository.countBySituacaoAndMonthAndYear(Situacao.ATENDIDO, mes, ano);
        Map<String, Long> resposta = new HashMap<>();
        resposta.put("totalAtendidos", totalAtendidos);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/estatisticas/relacao-situacao")
    public ResponseEntity<Map<Situacao, Long>> relacaoTicketsSituacaoAnual(@RequestParam int ano) {
        List<Object[]> resultados = chamadoRepository.countBySituacaoGroupedByYear(ano);
        Map<Situacao, Long> relacao = new HashMap<>();
        for (Object[] resultado : resultados) {
            Situacao situacao = (Situacao) resultado[0];
            Long quantidade = (Long) resultado[1];
            relacao.put(situacao, quantidade);
        }
        return ResponseEntity.ok(relacao);
    }

    @GetMapping("/estatisticas/coeficiente-atendimento")
    public ResponseEntity<Map<String, Double>> coeficienteAtendimento() {
        Double coeficiente = chamadoRepository.calcularCoeficienteAtendimento();
        Map<String, Double> resposta = new HashMap<>();
        resposta.put("coeficienteAtendimento", coeficiente != null ? coeficiente : 0.0);
        return ResponseEntity.ok(resposta);
    }

}