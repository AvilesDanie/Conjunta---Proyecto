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
public class ElectrodomesticoSeleccionado {
    private Long idElectrodomestico;
    private int cantidad;
    private int id;
    private String nombre;
    private double precio;
    private String fotoUrl;
    private int contador = 1; // Campo adicional para manejar el contador


    public ElectrodomesticoSeleccionado(String nombre, String modelo, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public ElectrodomesticoSeleccionado() {
    }
    
    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
    
    public Long getIdElectrodomestico() {
        return idElectrodomestico;
    }
    
    public void setIdElectrodomestico(Long idElectrodomestico) {
        this.idElectrodomestico = idElectrodomestico;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
