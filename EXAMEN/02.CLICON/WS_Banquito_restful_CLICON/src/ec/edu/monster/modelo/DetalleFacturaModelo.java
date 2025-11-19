package ec.edu.monster.modelo;

import java.math.BigDecimal;

public class DetalleFacturaModelo {

    private String codigoElectro;
    private String nombreElectro;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public String getCodigoElectro() {
        return codigoElectro;
    }

    public void setCodigoElectro(String codigoElectro) {
        this.codigoElectro = codigoElectro;
    }

    public String getNombreElectro() {
        return nombreElectro;
    }

    public void setNombreElectro(String nombreElectro) {
        this.nombreElectro = nombreElectro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
