/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.model;

/**
 *
 * @author danie
 */

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cuenta")
public class Cuenta {

    @Id
    @Column(name = "num_cuenta", length = 20)
    private String numCuenta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cedula_cliente")
    private Cliente cliente;

    @Column(name = "tipo_cuenta", length = 20, nullable = false)
    private String tipoCuenta; // AHORROS, CORRIENTE, etc.

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    // Getters y setters

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
