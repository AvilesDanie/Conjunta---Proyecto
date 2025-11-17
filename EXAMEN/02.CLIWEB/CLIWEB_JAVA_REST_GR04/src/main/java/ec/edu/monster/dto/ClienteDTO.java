package ec.edu.monster.dto;

public class ClienteDTO {

    public Long id;
    public String cedula;
    public String nombre;
    public String fechaNacimiento;   // "yyyy-MM-dd"
    public String estadoCivil;

    // Estos 2 campos son opcionales: úsalos si tu API los devuelve
    public String cuentaPrincipal;      // ej: "CTA-0002-2024"
    public String tipoCuentaPrincipal;  // ej: "CORRIENTE"

    // Helper para la letra del avatar (círculo azul con inicial)
    public String getInicial() {
        if (nombre == null || nombre.isBlank()) {
            return "?";
        }
        return nombre.trim().substring(0, 1).toUpperCase();
    }
}
