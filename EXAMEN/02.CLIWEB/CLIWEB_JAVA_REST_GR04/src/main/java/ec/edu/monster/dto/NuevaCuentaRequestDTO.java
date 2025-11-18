package ec.edu.monster.dto;

import java.math.BigDecimal;

/**
 * DTO simple para enviar la información al endpoint POST /cuentas.
 */
public class NuevaCuentaRequestDTO {
    public String cedulaCliente;
    public String tipoCuenta;
    public BigDecimal saldoInicial;
}
