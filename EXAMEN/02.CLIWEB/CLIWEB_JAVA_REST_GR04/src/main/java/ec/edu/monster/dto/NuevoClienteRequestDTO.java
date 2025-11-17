package ec.edu.monster.dto;

public class NuevoClienteRequestDTO {

    public String cedula;
    public String nombre;
    public String fechaNacimiento;   // formato yyyy-MM-dd
    public String estadoCivil;       // SOLTERO, CASADO, etc.
    public String tipoCuentaInicial; // AHORROS, CORRIENTE, etc.
}
