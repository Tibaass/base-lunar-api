package br.com.fiap.baselunar.controller;

import br.com.fiap.baselunar.dto.MedicaoRequestDTO;
import br.com.fiap.baselunar.dto.MedicaoResponseDTO;
import br.com.fiap.baselunar.service.MedicaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicoes")
public class MedicaoController {

    @Autowired
    private MedicaoService service;

    @PostMapping
    public ResponseEntity<MedicaoResponseDTO> registrar(@RequestBody MedicaoRequestDTO req) {
        try {
            return ResponseEntity.ok(service.registrar(req));
        } catch (RuntimeException e) {
            // Sensor inexistente cai aqui. Pra Sprint 2 ta ok devolver 404 simples.
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<MedicaoResponseDTO> listar() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicaoResponseDTO> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sensor/{id}")
    public List<MedicaoResponseDTO> listarPorSensor(@PathVariable Long id) {
        return service.listarPorSensor(id);
    }
}
