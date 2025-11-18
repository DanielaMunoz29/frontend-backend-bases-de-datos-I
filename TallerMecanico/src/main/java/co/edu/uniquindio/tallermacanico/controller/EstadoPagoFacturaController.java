package co.edu.uniquindio.tallermacanico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar los estados de pago de facturas.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/estados-pago")
public class EstadoPagoFacturaController {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Obtiene todos los estados de pago disponibles.
     *
     * @return lista de estados de pago con id y nombre
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> obtenerEstadosPago() {
        try {
            // Usar alias en minúsculas para que el frontend pueda leer los datos correctamente
            String sql = "SELECT id_estado_pago AS \"id_estado_pago\", " +
                    "nombre_estado AS \"nombre_estado\" " +
                    "FROM estado_pago_factura ORDER BY id_estado_pago";

            List<Map<String, Object>> estados = jdbcTemplate.queryForList(sql);

            System.out.println("Estados de pago obtenidos: " + estados);

            return ResponseEntity.ok(estados);
        } catch (Exception e) {
            // Si la tabla no existe o está vacía, devolver estados por defecto
            System.err.println("Error al obtener estados de pago: " + e.getMessage());
            e.printStackTrace();

            // Devolver lista vacía o estados por defecto
            List<Map<String, Object>> estadosDefault = List.of(
                    Map.of("id_estado_pago", 1, "nombre_estado", "Pendiente"),
                    Map.of("id_estado_pago", 2, "nombre_estado", "Pagado"),
                    Map.of("id_estado_pago", 3, "nombre_estado", "Cancelado")
            );

            return ResponseEntity.ok(estadosDefault);
        }
    }
}