/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author danie
 */

/**
 * Representa la respuesta de la API para clientes
 * (ClienteResponseDTO y ClienteOnlyResponseDTO)
 */
public class ClienteModelo {
    private String cedula;
    private String nombre;
    private String fechaNacimiento; // yyyy-MM-dd o null
    private String estadoCivil;
    private String numCuentaInicial;   // puede venir null
    private String tipoCuentaInicial;  // puede venir null

    // Getters y setters
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getNumCuentaInicial() {
        return numCuentaInicial;
    }

    public void setNumCuentaInicial(String numCuentaInicial) {
        this.numCuentaInicial = numCuentaInicial;
    }

    public String getTipoCuentaInicial() {
        return tipoCuentaInicial;
    }

    public void setTipoCuentaInicial(String tipoCuentaInicial) {
        this.tipoCuentaInicial = tipoCuentaInicial;
    }
}
