package co.edu.uniquindio.tallermacanico.controller;

import co.edu.uniquindio.tallermacanico.dto.ApiErrorResponse;
import co.edu.uniquindio.tallermacanico.model.Factura;
import co.edu.uniquindio.tallermacanico.repository.FacturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con las facturas.
 */
@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private static final Logger log = LoggerFactory.getLogger(FacturaController.class);
    private final FacturaRepository facturaRepository;

    public FacturaController(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    /**
     * Obtiene todas las facturas registradas en la base de datos.
     */
    @GetMapping
    public ResponseEntity<List<Factura>> obtenerFacturas() {
        try {
            List<Factura> facturas = facturaRepository.listarFacturas();
            log.info("Se obtuvieron {} facturas", facturas.size());
            return ResponseEntity.ok(facturas);
        } catch (Exception e) {
            log.error("Error al obtener facturas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Busca una factura por su identificador único.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        try {
            Factura factura = facturaRepository.buscarPorId(id);
            if (factura != null) {
                return ResponseEntity.ok(factura);
            }
            return ResponseEntity.status(404).body(
                    new ApiErrorResponse("Factura no encontrada", "ID: " + id)
            );
        } catch (Exception e) {
            log.error("Error al buscar factura por ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiErrorResponse("Error interno", e.getMessage())
            );
        }
    }

    /**
     * Registra una nueva factura en la base de datos.
     */
    @PostMapping
    public ResponseEntity<?> registrarFactura(@RequestBody Factura factura) {
        try {
            log.info("Recibiendo factura: {}", factura);

            // Validaciones básicas
            if (factura.getIdOrdenTrabajo() <= 0) {
                log.warn("ID de orden de trabajo inválido: {}", factura.getIdOrdenTrabajo());
                return ResponseEntity.badRequest().body(
                        new ApiErrorResponse("Datos inválidos", "El ID de la orden de trabajo debe ser mayor que cero")
                );
            }

            if (factura.getIdEstadoPago() <= 0) {
                log.warn("ID de estado de pago inválido: {}", factura.getIdEstadoPago());
                return ResponseEntity.badRequest().body(
                        new ApiErrorResponse("Datos inválidos", "El ID del estado de pago debe ser mayor que cero")
                );
            }

            if (factura.getFechaEmision() == null) {
                log.warn("Fecha de emisión nula");
                return ResponseEntity.badRequest().body(
                        new ApiErrorResponse("Datos inválidos", "La fecha de emisión es obligatoria")
                );
            }

            if (factura.getTotal() < 0) {
                log.warn("Total negativo: {}", factura.getTotal());
                return ResponseEntity.badRequest().body(
                        new ApiErrorResponse("Datos inválidos", "El total no puede ser negativo")
                );
            }

            // Registrar factura
            int idGenerado = facturaRepository.registrarFactura(factura);
            factura.setIdFactura(idGenerado);

            log.info("Factura registrada exitosamente con ID: {}", idGenerado);
            return ResponseEntity.ok(factura);

        } catch (Exception e) {
            log.error("Error al registrar factura", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiErrorResponse("Error al crear factura", "Detalle: " + e.getMessage())
            );
        }
    }

    /**
     * Elimina una factura existente por su identificador.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFactura(@PathVariable int id) {
        try {
            boolean eliminado = facturaRepository.eliminarFactura(id);
            if (eliminado) {
                log.info("Factura {} eliminada correctamente", id);
                return ResponseEntity.ok("Factura eliminada correctamente");
            }
            return ResponseEntity.status(404).body(
                    new ApiErrorResponse("Factura no encontrada", "ID: " + id)
            );
        } catch (Exception e) {
            log.error("Error al eliminar factura: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiErrorResponse("Error al eliminar", e.getMessage())
            );
        }
    }
}