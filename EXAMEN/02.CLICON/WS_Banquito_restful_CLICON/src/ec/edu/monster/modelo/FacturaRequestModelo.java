package ec.edu.monster.modelo;

import java.util.List;

public class FacturaRequestModelo {

    public String cedulaCliente;
    public String nombreCliente;

    // Formato antiguo (opcional, aquí no lo usaremos)
    public Long idElectrodomestico;
    public int cantidad;

    // Formato nuevo: múltiples productos
    public List<FacturaProductoRequestModelo> productos;

    public String formaPago;   // EFECTIVO o CREDITO

    // Solo si formaPago = CREDITO
    public int plazoMeses;
    public String numCuentaCredito;
}
