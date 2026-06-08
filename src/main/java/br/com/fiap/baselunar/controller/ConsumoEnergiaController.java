package br.com.fiap.baselunar.controller;

import br.com.fiap.baselunar.model.ConsumoEnergia;
import br.com.fiap.baselunar.service.ConsumoEnergiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumo-energia")
public class ConsumoEnergiaController {

    @Autowired
    private ConsumoEnergiaService service;

    @GetMapping
    public List<ConsumoEnergia> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsumoEnergia> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ConsumoEnergia criar(@RequestBody ConsumoEnergia consumo) {
        return service.salvar(consumo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsumoEnergia> atualizar(@PathVariable Long id, @RequestBody ConsumoEnergia consumo) {
        try {
            return ResponseEntity.ok(service.atualizar(id, consumo));
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
