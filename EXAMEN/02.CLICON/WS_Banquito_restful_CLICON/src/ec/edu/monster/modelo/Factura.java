/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Modelo para representar una Factura según el servidor RESTful
 * @author CLICON
 */
public class Factura {
    private Long id;
    private String fecha;
    private String cedulaCliente;
    private String nombreCliente;
    private String formaPago;
    private BigDecimal totalBruto;
    private BigDecimal descuento;
    private BigDecimal totalNeto;
    private Long idCreditoBanquito;
    private List<DetalleFactura> detalles;

    public Factura() {
    }

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
    
    // Alias para compatibilidad
    public Long getIdCredito() {
        return idCreditoBanquito;
    }
    
    public void setIdCredito(Long idCredito) {
        this.idCreditoBanquito = idCredito;
    }
    
    public BigDecimal getTotal() {
        return totalNeto;
    }
    
    // Alias getCedula para compatibilidad con vistas
    public String getCedula() {
        return cedulaCliente;
    }
    
    public void setCedula(String cedula) {
        this.cedulaCliente = cedula;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
    
    /**
     * Método de utilidad para imprimir la factura en consola
     */
    public void imprimirFactura() {
        final int nameWidth = 30;
        final int qtyWidth = 8;
        final int priceWidth = 12;
        
        String headerFmt = "%-" + nameWidth + "s %" + qtyWidth + "s %" + priceWidth + "s %" + priceWidth + "s%n";
        String rowFmt = "%-" + nameWidth + "s %" + qtyWidth + "d %" + priceWidth + ".2f %" + priceWidth + ".2f%n";
        
        int sepLen = nameWidth + qtyWidth + priceWidth * 2 + 4;
        String separator = new String(new char[sepLen]).replace('\0', '-');
        
        System.out.println("\n" + new String(new char[sepLen]).replace('\0', '='));
        System.out.println("                    FACTURA ELECTROQUITO");
        System.out.println(new String(new char[sepLen]).replace('\0', '='));
        System.out.printf("Factura No: %d%n", id);
        System.out.printf("Fecha: %s%n", fecha);
        System.out.printf("Cliente: %s%n", nombreCliente);
        System.out.printf("Cédula: %s%n", cedulaCliente);
        System.out.printf("Forma de Pago: %s%n", formaPago);
        if (idCreditoBanquito != null) {
            System.out.printf("ID Crédito: %d%n", idCreditoBanquito);
        }
        System.out.println(separator);
        
        // Header de la tabla
        System.out.printf(headerFmt, "Producto", "Cant.", "P. Unit.", "Subtotal");
        System.out.println(separator);
        
        // Filas de productos
        if (detalles != null) {
            for (DetalleFactura detalle : detalles) {
                String prodName = detalle.getNombreElectro();
                if (prodName.length() > nameWidth) {
                    prodName = prodName.substring(0, nameWidth - 3) + "...";
                }
                System.out.printf(rowFmt, 
                    prodName, 
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario().doubleValue(),
                    detalle.getSubtotal().doubleValue());
            }
        }
        
        System.out.println(separator);
        System.out.printf("%-" + (nameWidth + qtyWidth + priceWidth + 2) + "s %" + priceWidth + ".2f%n", 
            "TOTAL BRUTO:", totalBruto.doubleValue());
        System.out.printf("%-" + (nameWidth + qtyWidth + priceWidth + 2) + "s %" + priceWidth + ".2f%n", 
            "DESCUENTO:", descuento.doubleValue());
        System.out.printf("%-" + (nameWidth + qtyWidth + priceWidth + 2) + "s %" + priceWidth + ".2f%n", 
            "TOTAL A PAGAR:", totalNeto.doubleValue());
        System.out.println(new String(new char[sepLen]).replace('\0', '='));
    }
}
