package co.edu.uniquindio.tallermacanico.controller;

import co.edu.uniquindio.tallermacanico.dto.OrdenTrabajoDTO;
import co.edu.uniquindio.tallermacanico.model.OrdenTrabajo;
import co.edu.uniquindio.tallermacanico.repository.EstadoOrdenRepository;
import co.edu.uniquindio.tallermacanico.repository.OrdenTrabajoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenTrabajoController {
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final EstadoOrdenRepository estadoOrdenRepository;

    public OrdenTrabajoController(OrdenTrabajoRepository ordenTrabajoRepository,
                                  EstadoOrdenRepository estadoOrdenRepository) {
        this.ordenTrabajoRepository = ordenTrabajoRepository;
        this.estadoOrdenRepository = estadoOrdenRepository;
    }

    /**
     * Lista todas las órdenes de trabajo con su estado.
     */
    @GetMapping
    public ResponseEntity<List<OrdenTrabajoDTO>> obtenerOrdenes() {
        List<OrdenTrabajo> ordenes = ordenTrabajoRepository.listarOrdenesTrabajo();

        List<OrdenTrabajoDTO> dtos = ordenes.stream()
                .map(orden -> new OrdenTrabajoDTO(
                        orden.getIdOrdenTrabajo(),
                        orden.getIdVehiculo(),
                        orden.getFechaIngreso(),
                        orden.getFechaSalida(),
                        orden.getDiagnosticoInicial(),
                        estadoOrdenRepository.obtenerNombrePorId(orden.getIdEstadoOrden())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Crea una nueva orden de trabajo.
     */
    @PostMapping
    public ResponseEntity<OrdenTrabajo> crearOrden(@RequestBody OrdenTrabajo orden) {
        try {
            int idGenerado = ordenTrabajoRepository.registrarOrdenTrabajo(orden);
            orden.setIdOrdenTrabajo(idGenerado);
            return ResponseEntity.ok(orden);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza el estado de una orden de trabajo.
     * @param id ID de la orden
     * @param body Map con el campo "idEstadoOrden"
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable int id,
            @RequestBody Map<String, Integer> body) {
        try {
            Integer nuevoEstado = body.get("idEstadoOrden");
            if (nuevoEstado == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El campo 'idEstadoOrden' es requerido"));
            }

            // Validar que el estado sea válido (1=Registrada, 2=En Proceso, 3=Completada, 4=Cancelada)
            if (nuevoEstado < 1 || nuevoEstado > 4) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Estado inválido. Debe ser 1, 2, 3 o 4"));
            }

            // Verificar que la orden existe
            OrdenTrabajo ordenExistente = ordenTrabajoRepository.buscarPorId(id);
            if (ordenExistente == null) {
                return ResponseEntity.notFound().build();
            }

            // Actualizar el estado
            int filasActualizadas = ordenTrabajoRepository.actualizarEstado(id, nuevoEstado);

            if (filasActualizadas == 0) {
                return ResponseEntity.notFound().build();
            }

            // Si el estado es "Completada" (3), actualizar fecha de salida
            if (nuevoEstado == 3 && ordenExistente.getFechaSalida() == null) {
                ordenTrabajoRepository.actualizarFechaSalida(id, LocalDate.now());
            }

            String nombreEstado = estadoOrdenRepository.obtenerNombrePorId(nuevoEstado);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Estado actualizado correctamente",
                    "idOrden", id,
                    "nuevoEstado", nombreEstado
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al actualizar el estado: " + e.getMessage()));
        }
    }

    /**
     * Obtiene una orden específica por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerOrden(@PathVariable int id) {
        try {
            OrdenTrabajo orden = ordenTrabajoRepository.buscarPorId(id);
            if (orden == null) {
                return ResponseEntity.notFound().build();
            }

            OrdenTrabajoDTO dto = new OrdenTrabajoDTO(
                    orden.getIdOrdenTrabajo(),
                    orden.getIdVehiculo(),
                    orden.getFechaIngreso(),
                    orden.getFechaSalida(),
                    orden.getDiagnosticoInicial(),
                    estadoOrdenRepository.obtenerNombrePorId(orden.getIdEstadoOrden())
            );

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}