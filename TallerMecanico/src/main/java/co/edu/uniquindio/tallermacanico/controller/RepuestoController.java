package co.edu.uniquindio.tallermacanico.controller;

import co.edu.uniquindio.tallermacanico.model.Repuesto;
import co.edu.uniquindio.tallermacanico.repository.RepuestoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones sobre los repuestos del taller.
 * Expone endpoints para listar, registrar, consultar, eliminar y actualizar stock.
 */
@RestController
@RequestMapping("/api/repuestos")
public class RepuestoController {

    private final RepuestoRepository repuestoRepository;

    public RepuestoController(RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }

    /**
     * Lista todos los repuestos registrados en la base de datos.
     *
     * @return lista de objetos {@link Repuesto}
     */
    @GetMapping
    public ResponseEntity<List<Repuesto>> obtenerRepuestos() {
        return ResponseEntity.ok(repuestoRepository.listarRepuestos());
    }

    /**
     * Registra un nuevo repuesto en la base de datos.
     *
     * @param repuesto objeto {@link Repuesto} con los datos a insertar
     * @return objeto {@link Repuesto} con el ID generado
     */
    @PostMapping
    public ResponseEntity<Repuesto> registrarRepuesto(@RequestBody Repuesto repuesto) {
        int idGenerado = repuestoRepository.registrarRepuesto(repuesto);
        repuesto.setIdRepuesto(idGenerado);
        return ResponseEntity.ok(repuesto);
    }

    /**
     * Consulta un repuesto por su identificador único.
     *
     * @param id identificador del repuesto
     * @return objeto {@link Repuesto} si existe, o {@code 404 Not Found} si no se encuentra
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRepuestoPorId(@PathVariable int id) {
        Repuesto repuesto = repuestoRepository.buscarPorId(id);
        if (repuesto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repuesto);
    }

    /**
     * Elimina un repuesto por su identificador.
     *
     * @param id identificador del repuesto
     * @return {@code 200 OK} si se elimina correctamente, o {@code 404 Not Found} si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRepuesto(@PathVariable int id) {
        Repuesto repuesto = repuestoRepository.buscarPorId(id);
        if (repuesto == null) {
            return ResponseEntity.notFound().build();
        }
        repuestoRepository.eliminarRepuesto(id);
        return ResponseEntity.ok("Repuesto eliminado correctamente");
    }

    /**
     * Actualiza el stock de un repuesto existente.
     *
     * @param id identificador del repuesto
     * @param nuevoStock nuevo valor de stock
     * @return {@code 200 OK} si se actualiza correctamente, o {@code 404 Not Found} si no existe
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable int id, @RequestParam double nuevoStock) {
        Repuesto repuesto = repuestoRepository.buscarPorId(id);
        if (repuesto == null) {
            return ResponseEntity.notFound().build();
        }
        repuestoRepository.actualizarStock(id, nuevoStock);
        return ResponseEntity.ok("Stock actualizado correctamente");
    }
}
