package ec.edu.monster.dto;

public class CuentaDTO {

    public Long id;
    public String numero;          // Ej: "CTA-0002-2024"
    public String tipo;            // AHORROS / CORRIENTE
    public double saldo;

    // Datos básicos del cliente asociado
    public String clienteNombre;   // Ej: "Carlos Alberto Mendoza Torres"
    public String clienteCedula;   // Ej: "0987654321"
}
