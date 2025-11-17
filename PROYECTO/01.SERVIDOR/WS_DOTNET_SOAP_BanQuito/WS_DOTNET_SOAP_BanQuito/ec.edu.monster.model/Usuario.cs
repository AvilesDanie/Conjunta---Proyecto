using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model
{
    [Table("usuario")]
    public class Usuario
    {
        [Key]
        [Column("id")]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long Id { get; set; }

        [Required]
        [Column("username")]
        [StringLength(50)]
        public string Username { get; set; }

        [Required]
        [Column("password")]
        [StringLength(100)]
        public string Password { get; set; } // (idealmente hash)

        [Required]
        [Column("rol")]
        [StringLength(20)]
        public string Rol { get; set; } // SUPERADMIN, ADMIN, USER

        [Required]
        [Column("activo")]
        public bool Activo { get; set; } = true;
    }
}

