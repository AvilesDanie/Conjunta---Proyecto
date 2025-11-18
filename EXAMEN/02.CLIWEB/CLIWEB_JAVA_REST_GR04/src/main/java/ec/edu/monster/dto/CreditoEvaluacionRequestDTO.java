package ec.edu.monster.dto;

import java.math.BigDecimal;

public class CreditoEvaluacionRequestDTO {
    public String cedula;
    public BigDecimal precioProducto;
    public int plazoMeses;
    public String numCuentaCredito;

    public CreditoEvaluacionRequestDTO() {
    }
}
