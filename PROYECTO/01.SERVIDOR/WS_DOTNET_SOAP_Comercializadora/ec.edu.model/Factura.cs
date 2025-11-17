using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Numerics;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model
{
    [Table("factura")]
    public class Factura
    {
        [Key]
        [Column("id")]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long Id { get; set; }

        [Column("fecha")]
        public DateTime Fecha { get; set; }

        [Required]
        [Column("cedula_cliente")]
        [StringLength(10)]
        public string CedulaCliente { get; set; }

        [Required]
        [Column("nombre_cliente")]
        [StringLength(100)]
        public string NombreCliente { get; set; }

        [Required]
        [Column("forma_pago")]
        [StringLength(15)]
        public string FormaPago { get; set; } // EFECTIVO / CREDITO

        [Required]
        [Column("total_bruto", TypeName = "decimal")]
        public decimal TotalBruto { get; set; }

        [Required]
        [Column("descuento", TypeName = "decimal")]
        public decimal Descuento { get; set; }

        [Required]
        [Column("total_neto", TypeName = "decimal")]
        public decimal TotalNeto { get; set; }

        [Column("id_credito")]
        public long? IdCreditoBanquito { get; set; }

        // RELACIÓN 1-N
        public ICollection<DetalleFactura> Detalles { get; set; }
    }
}
