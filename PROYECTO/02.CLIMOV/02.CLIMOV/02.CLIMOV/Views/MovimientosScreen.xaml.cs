using _02.CLIMOV.Helpers;
using _02.CLIMOV.Services;
using _02.CLIMOV.Models;

namespace _02.CLIMOV.Views
{
    [QueryProperty(nameof(NumeroCuenta), "numeroCuenta")]
    public partial class MovimientosScreen : ContentPage
    {
        private readonly BanquitoSoapService _service;
        private string _numeroCuenta;

        public string NumeroCuenta
        {
            get => _numeroCuenta;
            set
            {
                _numeroCuenta = value;
                CargarMovimientos();
            }
        }

        public MovimientosScreen()
        {
            InitializeComponent();
            _service = new BanquitoSoapService();
        }

        private async void CargarMovimientos()
        {
            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;

            try
            {
                LabelNumeroCuenta.Text = _numeroCuenta;
                
                var cuenta = await _service.ObtenerCuentaAsync(_numeroCuenta);
                if (cuenta != null)
                {
                    LabelSaldo.Text = $"$ {cuenta.Saldo:N2}";
                }

                var movimientos = await _service.ObtenerMovimientosPorCuentaAsync(_numeroCuenta);
                CollectionMovimientos.ItemsSource = movimientos;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar movimientos: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
            }
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnNuevoMovimientoClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync($"crear_movimiento?numeroCuenta={_numeroCuenta}");
        }
    }
}
