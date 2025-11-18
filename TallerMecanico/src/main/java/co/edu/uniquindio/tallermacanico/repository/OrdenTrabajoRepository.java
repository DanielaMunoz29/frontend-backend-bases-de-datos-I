package co.edu.uniquindio.tallermacanico.repository;

import co.edu.uniquindio.tallermacanico.model.OrdenServicio;
import co.edu.uniquindio.tallermacanico.model.OrdenTrabajo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class OrdenTrabajoRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrdenTrabajoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lista todas las órdenes de trabajo registradas en la base de datos.
     * @return lista de órdenes de trabajo
     */
    public List<OrdenTrabajo> listarOrdenesTrabajo() {
        String sql = "SELECT * FROM orden_trabajo";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OrdenTrabajo.class));
    }

    /**
     * Busca una orden de trabajo por su ID.
     * @param id identificador de la orden
     * @return orden encontrada
     */
    public OrdenTrabajo buscarPorId(int id) {
        String sql = "SELECT * FROM orden_trabajo WHERE id_orden_trabajo = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(OrdenTrabajo.class), id);
    }

    /**
     * Registra una nueva orden de trabajo en la base de datos.
     * @param orden objeto con los datos de la orden
     * @return ID generado de la orden
     */
    public int registrarOrdenTrabajo(OrdenTrabajo orden) {
        String sql = "INSERT INTO orden_trabajo (id_vehiculo, fecha_ingreso, fecha_salida, diagnostico_inicial, id_estado_orden) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_orden_trabajo"});
            ps.setInt(1, orden.getIdVehiculo());
            ps.setDate(2, java.sql.Date.valueOf(orden.getFechaIngreso()));
            ps.setDate(3, orden.getFechaSalida() != null ? java.sql.Date.valueOf(orden.getFechaSalida()) : null);
            ps.setString(4, orden.getDiagnosticoInicial());
            ps.setInt(5, orden.getIdEstadoOrden());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    /**
     * Actualiza el estado de una orden de trabajo.
     * @param idOrden identificador de la orden
     * @param nuevoEstado nuevo ID del estado
     * @return número de filas afectadas
     */
    public int actualizarEstado(int idOrden, int nuevoEstado) {
        String sql = "UPDATE orden_trabajo SET id_estado_orden = ? WHERE id_orden_trabajo = ?";
        return jdbcTemplate.update(sql, nuevoEstado, idOrden);
    }

    /**
     * Actualiza la fecha de salida de una orden cuando se completa.
     * @param idOrden identificador de la orden
     * @param fechaSalida fecha de salida
     * @return número de filas afectadas
     */
    public int actualizarFechaSalida(int idOrden, java.time.LocalDate fechaSalida) {
        String sql = "UPDATE orden_trabajo SET fecha_salida = ? WHERE id_orden_trabajo = ?";
        return jdbcTemplate.update(sql, java.sql.Date.valueOf(fechaSalida), idOrden);
    }

    /**
     * Elimina una orden de trabajo por su ID.
     * @param id identificador de la orden
     */
    public void eliminarOrdenTrabajo(int id) {
        String sql = "DELETE FROM orden_trabajo WHERE id_orden_trabajo = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Obtiene todas las órdenes de servicio junto con el nombre del servicio y el estado.
     *
     * @return lista de objetos OrdenServicio enriquecidos con datos adicionales
     */
    public List<OrdenServicio> listarOrdenesConDetalles() {
        String sql = "SELECT os.id_orden_servicio, os.id_orden_trabajo, os.id_servicio, " +
                "s.nombre AS nombre_servicio, os.estado, os.precio_final " +
                "FROM orden_servicio os " +
                "JOIN servicio s ON os.id_servicio = s.id_servicio";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OrdenServicio.class));
    }
}