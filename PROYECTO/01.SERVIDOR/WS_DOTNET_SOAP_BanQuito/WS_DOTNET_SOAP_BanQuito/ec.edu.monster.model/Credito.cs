using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model
{
    [Table("credito")]
    public class Credito
    {
        [Key]
        [Column("id")]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long Id { get; set; }

        [Column("cedula_cliente")]
        [StringLength(10)]
        [ForeignKey("Cliente")]
        public string CedulaCliente { get; set; }

        public virtual Cliente Cliente { get; set; }

        [Column("num_cuenta_asociada")]
        [StringLength(20)]
        [ForeignKey("CuentaAsociada")]
        public string NumCuentaAsociada { get; set; }

        public virtual Cuenta CuentaAsociada { get; set; }

        [Required]
        [Column("monto", TypeName = "decimal")]
        public decimal Monto { get; set; }  // (10,2)

        [Required]
        [Column("plazo_meses")]
        public int PlazoMeses { get; set; }

        [Required]
        [Column("tasa_anual", TypeName = "decimal")]
        public decimal TasaAnual { get; set; } // 16.00

        [Column("fecha_aprobacion", TypeName = "date")]
        public DateTime? FechaAprobacion { get; set; }

        [Column("estado")]
        [StringLength(15)]
        public string Estado { get; set; } // APROBADO, RECHAZADO, etc.

        public virtual ICollection<CuotaAmortizacion> Cuotas { get; set; }
    }
}
