package br.com.fiap.baselunar.controller;

import br.com.fiap.baselunar.model.Climatizacao;
import br.com.fiap.baselunar.service.ClimatizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/climatizacao")
public class ClimatizacaoController {

    @Autowired
    private ClimatizacaoService service;

    @GetMapping
    public List<Climatizacao> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Climatizacao> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Climatizacao criar(@RequestBody Climatizacao climatizacao) {
        return service.salvar(climatizacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Climatizacao> atualizar(@PathVariable Long id, @RequestBody Climatizacao climatizacao) {
        try {
            return ResponseEntity.ok(service.atualizar(id, climatizacao));
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
