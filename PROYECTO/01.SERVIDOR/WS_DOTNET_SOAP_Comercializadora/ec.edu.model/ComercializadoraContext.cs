using System.Data.Entity;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.model
{
    public class ComercializadoraContext : DbContext
    {
        // Usa la connection string "ComercializadoraContext" del Web.config
        public ComercializadoraContext() : base("name=ComercializadoraContext")
        {
        }

        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<Electrodomestico> Electrodomesticos { get; set; }
        public DbSet<Factura> Facturas { get; set; }
        public DbSet<DetalleFactura> DetallesFactura { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // Decimal configs (10,2)
            modelBuilder.Entity<Electrodomestico>()
                .Property(e => e.PrecioVenta)
                .HasPrecision(10, 2);

            modelBuilder.Entity<Factura>()
                .Property(f => f.TotalBruto)
                .HasPrecision(10, 2);

            modelBuilder.Entity<Factura>()
                .Property(f => f.Descuento)
                .HasPrecision(10, 2);

            modelBuilder.Entity<Factura>()
                .Property(f => f.TotalNeto)
                .HasPrecision(10, 2);

            modelBuilder.Entity<DetalleFactura>()
                .Property(d => d.PrecioUnitario)
                .HasPrecision(10, 2);

            modelBuilder.Entity<DetalleFactura>()
                .Property(d => d.Subtotal)
                .HasPrecision(10, 2);

            // RELACIÓN 1-N: Factura -> DetalleFactura
            modelBuilder.Entity<Factura>()
                .HasMany(f => f.Detalles)
                .WithRequired(d => d.Factura)
                .HasForeignKey(d => d.IdFactura)
                .WillCascadeOnDelete(true);

            // RELACIÓN N-1: DetalleFactura → Electrodomestico
            modelBuilder.Entity<DetalleFactura>()
                .HasRequired(d => d.Electrodomestico)
                .WithMany()
                .HasForeignKey(d => d.IdElectrodomestico);
        }
    }
}
