using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model
{
    [Table("detalle_factura")]
    public class DetalleFactura
    {
        [Key]
        [Column("id")]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long Id { get; set; }

        [Required]
        [Column("id_factura")]
        public long IdFactura { get; set; }

        [ForeignKey("IdFactura")]
        public Factura Factura { get; set; }

        [Required]
        [Column("id_electrodomestico")]
        public long IdElectrodomestico { get; set; }

        [ForeignKey("IdElectrodomestico")]
        public Electrodomestico Electrodomestico { get; set; }

        [Required]
        [Column("cantidad")]
        public int Cantidad { get; set; }

        [Required]
        [Column("precio_unitario", TypeName = "decimal")]
        public decimal PrecioUnitario { get; set; }

        [Required]
        [Column("subtotal", TypeName = "decimal")]
        public decimal Subtotal { get; set; }
    }
}
