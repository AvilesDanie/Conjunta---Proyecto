package ec.edu.monster.dto;

import jakarta.json.bind.annotation.JsonbProperty;

import java.math.BigDecimal;

/**
 * DTO simple para representar las facturas devueltas por el API de comercializadora.
 * Incluye los campos que la interfaz web necesita para listar el historial.
 */
public class FacturaDTO {

    private Long id;

    @JsonbProperty("fecha")
    private String fechaEmision;

    @JsonbProperty("nombreCliente")
    private String clienteNombre;

    @JsonbProperty("cedulaCliente")
    private String clienteCedula;

    @JsonbProperty("totalNeto")
    private BigDecimal total;

    public FacturaDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteCedula() {
        return clienteCedula;
    }

    public void setClienteCedula(String clienteCedula) {
        this.clienteCedula = clienteCedula;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
