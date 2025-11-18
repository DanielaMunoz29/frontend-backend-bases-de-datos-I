package co.edu.uniquindio.tallermacanico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenTrabajoDTO {
    private int idOrdenTrabajo;
    private int idVehiculo;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private String diagnosticoInicial;
    private String nombreEstado;
}
