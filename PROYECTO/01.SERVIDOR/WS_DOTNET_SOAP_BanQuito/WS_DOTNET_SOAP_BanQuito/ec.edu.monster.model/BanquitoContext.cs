using System.Data.Entity;

namespace WS_DOTNET_SOAP_BanQuito.ec.edu.monster.model
{
    public class BanquitoContext : DbContext
    {
        // Usa la connection string "BanquitoContext" del Web.config
        public BanquitoContext() : base("name=BanquitoContext")
        {
        }

        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<Cliente> Clientes { get; set; }
        public DbSet<Cuenta> Cuentas { get; set; }
        public DbSet<Movimiento> Movimientos { get; set; }
        public DbSet<Credito> Creditos { get; set; }
        public DbSet<CuotaAmortizacion> CuotasAmortizacion { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // Ajustes de precisión para decimales (10,2)
            modelBuilder.Entity<Cuenta>()
                .Property(c => c.Saldo)
                .HasPrecision(10, 2);

            modelBuilder.Entity<Credito>()
                .Property(c => c.Monto)
                .HasPrecision(10, 2);
            modelBuilder.Entity<Credito>()
                .Property(c => c.TasaAnual)
                .HasPrecision(5, 2);

            modelBuilder.Entity<CuotaAmortizacion>()
                .Property(c => c.ValorCuota).HasPrecision(10, 2);
            modelBuilder.Entity<CuotaAmortizacion>()
                .Property(c => c.InteresPagado).HasPrecision(10, 2);
            modelBuilder.Entity<CuotaAmortizacion>()
                .Property(c => c.CapitalPagado).HasPrecision(10, 2);
            modelBuilder.Entity<CuotaAmortizacion>()
                .Property(c => c.Saldo).HasPrecision(10, 2);

            modelBuilder.Entity<Movimiento>()
                .Property(m => m.Valor).HasPrecision(10, 2);
        }
    }
}
