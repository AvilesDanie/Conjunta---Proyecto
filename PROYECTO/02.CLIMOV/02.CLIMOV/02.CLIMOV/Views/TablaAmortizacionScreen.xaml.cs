namespace _02.CLIMOV.Views
{
    [QueryProperty(nameof(NumeroCuenta), "numeroCuenta")]
    [QueryProperty(nameof(Monto), "monto")]
    [QueryProperty(nameof(Plazo), "plazo")]
    [QueryProperty(nameof(Tasa), "tasa")]
    public partial class TablaAmortizacionScreen : ContentPage
    {
        public string NumeroCuenta { get; set; }
        public string Monto { get; set; }
        public string Plazo { get; set; }
        public string Tasa { get; set; }

        public TablaAmortizacionScreen()
        {
            InitializeComponent();
        }

        protected override void OnAppearing()
        {
            base.OnAppearing();
            GenerarTabla();
        }

        private void GenerarTabla()
        {
            if (!decimal.TryParse(Monto, out decimal monto) ||
                !int.TryParse(Plazo, out int plazo) ||
                !decimal.TryParse(Tasa, out decimal tasa))
                return;

            LabelMonto.Text = $"$ {monto:N2}";
            LabelTasa.Text = $"{tasa}%";
            LabelPlazo.Text = $"{plazo} meses";

            decimal tasaMensual = tasa / 100 / 12;
            decimal cuotaMensual = monto * (tasaMensual * (decimal)Math.Pow((double)(1 + tasaMensual), plazo)) / 
                                   ((decimal)Math.Pow((double)(1 + tasaMensual), plazo) - 1);

            LabelCuotaMensual.Text = $"$ {cuotaMensual:N2}";

            var tabla = new List<ItemTablaAmortizacion>();
            decimal saldoPendiente = monto;

            for (int i = 1; i <= plazo; i++)
            {
                decimal interes = saldoPendiente * tasaMensual;
                decimal capital = cuotaMensual - interes;
                saldoPendiente -= capital;

                tabla.Add(new ItemTablaAmortizacion
                {
                    NumeroCuota = i,
                    CuotaMensual = cuotaMensual,
                    Interes = interes,
                    Capital = capital,
                    SaldoPendiente = saldoPendiente > 0 ? saldoPendiente : 0
                });
            }

            CollectionTabla.ItemsSource = tabla;
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }
    }

    public class ItemTablaAmortizacion
    {
        public int NumeroCuota { get; set; }
        public decimal CuotaMensual { get; set; }
        public decimal Interes { get; set; }
        public decimal Capital { get; set; }
        public decimal SaldoPendiente { get; set; }
    }
}
