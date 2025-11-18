package ec.edu.monster.dto;

/**
 * DTO que representa las cuentas que devuelve el API BanQuito.
 * Los nombres de campo coinciden con el JSON del backend para evitar nulls.
 */
public class CuentaDTO {

    public String numCuenta;          // Ej: "CTA-0002-2024"
    public String tipoCuenta;         // AHORROS / CORRIENTE
    public double saldo;

    public String nombreCliente;      // Ej: "Carlos Alberto Mendoza Torres"
    public String cedulaCliente;      // Ej: "0987654321"
}
