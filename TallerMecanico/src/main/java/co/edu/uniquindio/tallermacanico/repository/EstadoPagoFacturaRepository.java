package co.edu.uniquindio.tallermacanico.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Repositorio para gestionar los estados de pago de facturas.
 */
@Repository
public class EstadoPagoFacturaRepository {

    private final JdbcTemplate jdbcTemplate;

    public EstadoPagoFacturaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lista todos los estados de pago disponibles.
     * @return lista de mapas con id_estado_pago y nombre_estado
     */
    public List<Map<String, Object>> listarEstadosPago() {
        String sql = "SELECT id_estado_pago, nombre_estado FROM estado_pago_factura ORDER BY id_estado_pago";
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * Obtiene el nombre de un estado por su ID.
     * @param idEstado identificador del estado
     * @return nombre del estado
     */
    public String obtenerNombrePorId(int idEstado) {
        String sql = "SELECT nombre_estado FROM estado_pago_factura WHERE id_estado_pago = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, idEstado);
        } catch (Exception e) {
            return "Desconocido";
        }
    }
}
