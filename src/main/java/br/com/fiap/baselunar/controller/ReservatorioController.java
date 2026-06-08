package br.com.fiap.baselunar.controller;

import br.com.fiap.baselunar.model.Reservatorio;
import br.com.fiap.baselunar.service.ReservatorioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservatorios")
public class ReservatorioController {

    @Autowired
    private ReservatorioService service;

    @GetMapping
    public List<Reservatorio> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservatorio> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Reservatorio criar(@Valid @RequestBody Reservatorio reservatorio) {
        return service.salvar(reservatorio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservatorio> atualizar(@PathVariable Long id, @RequestBody Reservatorio reservatorio) {
        try {
            return ResponseEntity.ok(service.atualizar(id, reservatorio));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
