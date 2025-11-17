using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model
{
    [Table("cuota_amortizacion")]
    public class CuotaAmortizacion
    {
        [Key]
        [Column("id")]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long Id { get; set; }

        [Column("id_credito")]
        [ForeignKey("Credito")]
        public long IdCredito { get; set; }

        public virtual Credito Credito { get; set; }

        [Required]
        [Column("numero_cuota")]
        public int NumeroCuota { get; set; }

        [Required]
        [Column("valor_cuota", TypeName = "decimal")]
        public decimal ValorCuota { get; set; }

        [Required]
        [Column("interes_pagado", TypeName = "decimal")]
        public decimal InteresPagado { get; set; }

        [Required]
        [Column("capital_pagado", TypeName = "decimal")]
        public decimal CapitalPagado { get; set; }

        [Required]
        [Column("saldo", TypeName = "decimal")]
        public decimal Saldo { get; set; }

        [Column("fecha_vencimiento", TypeName = "date")]
        public DateTime? FechaVencimiento { get; set; }

        [Column("estado")]
        [StringLength(15)]
        public string Estado { get; set; } // PENDIENTE, PAGADA, etc.
    }
}
