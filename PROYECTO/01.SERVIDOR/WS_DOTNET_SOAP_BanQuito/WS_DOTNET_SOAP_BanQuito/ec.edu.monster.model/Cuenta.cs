using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model
{
    [Table("cuenta")]
    public class Cuenta
    {
        [Key]
        [Column("num_cuenta")]
        [StringLength(20)]
        public string NumCuenta { get; set; }

        [Column("cedula_cliente")]
        [StringLength(10)]
        [ForeignKey("Cliente")]
        public string CedulaCliente { get; set; }

        public virtual Cliente Cliente { get; set; }

        [Required]
        [Column("tipo_cuenta")]
        [StringLength(20)]
        public string TipoCuenta { get; set; } // AHORROS, CORRIENTE, etc.

        [Required]
        [Column("saldo", TypeName = "decimal")]
        public decimal Saldo { get; set; } = 0m;

        // Navegación
        public virtual ICollection<Movimiento> Movimientos { get; set; }
        public virtual ICollection<Credito> CreditosAsociados { get; set; }
    }
}
