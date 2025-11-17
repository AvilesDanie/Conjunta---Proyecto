package ec.edu.monster.dto;

public class CuotaCreditoDTO {
    public Long id;
    public int numeroCuota;
    public double valorCuota;
    public double interes;
    public double capital;
    public double saldoPendiente;
    public String estado; // PENDIENTE / PAGADA, etc.

    public CuotaCreditoDTO() {
    }
}
