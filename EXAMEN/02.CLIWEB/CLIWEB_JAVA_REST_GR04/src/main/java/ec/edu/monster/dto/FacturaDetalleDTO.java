package ec.edu.monster.dto;

import jakarta.json.bind.annotation.JsonbProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO completo para el detalle de una factura, incluyendo productos y forma de pago.
 */
public class FacturaDetalleDTO {

    private Long id;

    @JsonbProperty("fecha")
    private String fechaEmision;

    @JsonbProperty("nombreCliente")
    private String clienteNombre;

    @JsonbProperty("cedulaCliente")
    private String clienteCedula;

    @JsonbProperty("totalNeto")
    private BigDecimal total;

    private String formaPago;
    
    private Integer plazoMeses;
    
    private String numCuentaCredito;

    @JsonbProperty("detalles")
    private List<DetalleProductoDTO> productos;

    public static class DetalleProductoDTO {
        private Long id;
        private String nombreElectrodomestico;
        private String nombreElectro;
        private String nombre;
        private String producto;
        private String electrodomestico;
        private String codigoElectro;
        private int cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;

        public DetalleProductoDTO() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombreElectrodomestico() {
            return nombreElectro != null ? nombreElectro
                : (nombreElectrodomestico != null ? nombreElectrodomestico 
                : (nombre != null ? nombre 
                : (producto != null ? producto : electrodomestico)));
        }

        public void setNombreElectrodomestico(String nombreElectrodomestico) {
            this.nombreElectrodomestico = nombreElectrodomestico;
        }

        public String getNombreElectro() {
            return nombreElectro;
        }

        public void setNombreElectro(String nombreElectro) {
            this.nombreElectro = nombreElectro;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getProducto() {
            return producto;
        }

        public void setProducto(String producto) {
            this.producto = producto;
        }

        public String getElectrodomestico() {
            return electrodomestico;
        }

        public void setElectrodomestico(String electrodomestico) {
            this.electrodomestico = electrodomestico;
        }

        public String getCodigoElectro() {
            return codigoElectro;
        }

        public void setCodigoElectro(String codigoElectro) {
            this.codigoElectro = codigoElectro;
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

    public FacturaDetalleDTO() {
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

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public String getNumCuentaCredito() {
        return numCuentaCredito;
    }

    public void setNumCuentaCredito(String numCuentaCredito) {
        this.numCuentaCredito = numCuentaCredito;
    }

    public List<DetalleProductoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<DetalleProductoDTO> productos) {
        this.productos = productos;
    }
}
