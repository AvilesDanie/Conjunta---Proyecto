using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model

{
    [Table("cliente")]
    public class Cliente
    {
        [Key]
        [Column("cedula")]
        [StringLength(10)]
        public string Cedula { get; set; }

        [Required]
        [Column("nombre")]
        [StringLength(100)]
        public string Nombre { get; set; }

        [Column("fecha_nacimiento", TypeName = "date")]
        public DateTime? FechaNacimiento { get; set; }

        [Column("estado_civil")]
        [StringLength(10)]
        public string EstadoCivil { get; set; } // SOLTERO, CASADO, etc.

        // Navegación
        public virtual ICollection<Cuenta> Cuentas { get; set; }
        public virtual ICollection<Credito> Creditos { get; set; }
    }
}
