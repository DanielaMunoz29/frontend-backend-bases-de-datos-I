package co.edu.uniquindio.tallermacanico.controller;

import co.edu.uniquindio.tallermacanico.repository.EstadoOrdenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar estados de orden.
 */
@RestController
@RequestMapping("/api/estados-orden")
public class EstadoOrdenController {

    private final EstadoOrdenRepository estadoOrdenRepository;

    public EstadoOrdenController(EstadoOrdenRepository estadoOrdenRepository) {
        this.estadoOrdenRepository = estadoOrdenRepository;
    }

    /**
     * Obtiene todos los estados de orden disponibles.
     *
     * @return lista de estados con id y nombre
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarEstados() {
        return ResponseEntity.ok(estadoOrdenRepository.listarEstadosOrden());
    }
}