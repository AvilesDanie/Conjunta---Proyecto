/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author danie
 */

import java.math.BigDecimal;

/**
 * Debe coincidir con ClienteRequestDTO del servicio REST
 */
public class ClienteCrearRequest {
    public String cedula;
    public String nombre;
    public String fechaNacimiento;   // yyyy-MM-dd
    public String estadoCivil;
    public String tipoCuentaInicial; // AHORROS, CORRIENTE, etc.
    public BigDecimal saldoInicial;  // puede ser null
}
