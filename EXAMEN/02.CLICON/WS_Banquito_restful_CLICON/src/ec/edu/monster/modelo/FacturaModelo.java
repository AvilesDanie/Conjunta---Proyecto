package ec.edu.monster.modelo;

import java.math.BigDecimal;
import java.util.List;

public class FacturaModelo {

    private Long id;
    private String fecha;
    private String cedulaCliente;
    private String nombreCliente;
    private String formaPago;
    private BigDecimal totalBruto;
    private BigDecimal descuento;
    private BigDecimal totalNeto;
    private Long idCreditoBanquito;
    private List<DetalleFacturaModelo> detalles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public BigDecimal getTotalBruto() {
        return totalBruto;
    }

    public void setTotalBruto(BigDecimal totalBruto) {
        this.totalBruto = totalBruto;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotalNeto() {
        return totalNeto;
    }

    public void setTotalNeto(BigDecimal totalNeto) {
        this.totalNeto = totalNeto;
    }

    public Long getIdCreditoBanquito() {
        return idCreditoBanquito;
    }

    public void setIdCreditoBanquito(Long idCreditoBanquito) {
        this.idCreditoBanquito = idCreditoBanquito;
    }

    public List<DetalleFacturaModelo> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFacturaModelo> detalles) {
        this.detalles = detalles;
    }
}
