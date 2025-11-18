/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.modelo;

/**
 *
 * @author ckan1
 */
public class Electrodomestico {
    private Long id;
    private String codigo;
    private String nombre;
    private double precioVenta;
    private String imagenUrl;

    public Electrodomestico(String nombre, double precioVenta) {
        this.nombre = nombre;
        this.precioVenta = precioVenta;
    }

    public Electrodomestico() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    // Alias para compatibilidad
    public double getPrecio() {
        return precioVenta;
    }
    
    public void setPrecio(double precio) {
        this.precioVenta = precio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
