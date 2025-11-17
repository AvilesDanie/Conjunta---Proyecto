using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Numerics;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model
{
    [Table("electrodomestico")]
    public class Electrodomestico
    {
        [Key]
        [Column("id")]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long Id { get; set; }

        [Required]
        [Column("codigo")]
        [StringLength(50)]
        public string Codigo { get; set; }

        [Required]
        [Column("nombre")]
        [StringLength(150)]
        public string Nombre { get; set; }

        [Required]
        [Column("precio_venta", TypeName = "decimal")]
        public decimal PrecioVenta { get; set; }
    }
}
