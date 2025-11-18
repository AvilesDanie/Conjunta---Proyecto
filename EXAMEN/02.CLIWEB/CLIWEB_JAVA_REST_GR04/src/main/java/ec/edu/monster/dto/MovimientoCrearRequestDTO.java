package ec.edu.monster.dto;

import java.math.BigDecimal;

/**
 * DTO para registrar movimientos (DEP/RET/TRA) en el API de BanQuito.
 */
public class MovimientoCrearRequestDTO {
    public String tipo;
    public BigDecimal valor;
    public String fecha;

    public String numCuenta;
    public String numCuentaOrigen;
    public String numCuentaDestino;
}
