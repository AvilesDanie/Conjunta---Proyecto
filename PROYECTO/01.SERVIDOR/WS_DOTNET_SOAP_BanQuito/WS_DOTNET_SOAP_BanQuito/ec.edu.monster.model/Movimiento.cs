using System;
using System.Collections.Generic;
using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model
{
    [Table("movimiento")]
    public class Movimiento
    {
        [Key]
        [Column("id")]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long Id { get; set; }

        [Column("num_cuenta")]
        [StringLength(20)]
        [ForeignKey("Cuenta")]
        public string NumCuenta { get; set; }

        public virtual Cuenta Cuenta { get; set; }

        [Required]
        [Column("tipo")]
        [StringLength(3)]
        public string Tipo { get; set; } // DEP, RET, TRA

        [Required]
        [Column("naturaleza")]
        [StringLength(10)]
        public string Naturaleza { get; set; } // INGRESO, EGRESO

        [Required]
        [Column("interno_transferencia")]
        public bool InternoTransferencia { get; set; } = false;

        [Required]
        [Column("valor", TypeName = "decimal")]
        public decimal Valor { get; set; }

        [Required]
        [Column("fecha", TypeName = "date")]
        public DateTime Fecha { get; set; }
    }
}
