using _02.CLIMOV.Helpers;
using _02.CLIMOV.Models;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    [QueryProperty(nameof(NumeroCuenta), "numeroCuenta")]
    public partial class CrearMovimientoScreen : ContentPage
    {
        private readonly BanquitoSoapService _service;
        private string _numeroCuenta;

        public string NumeroCuenta
        {
            get => _numeroCuenta;
            set
            {
                _numeroCuenta = value;
                EntryNumeroCuenta.Text = value;
            }
        }

        public CrearMovimientoScreen()
        {
            InitializeComponent();
            _service = new BanquitoSoapService();
            PickerTipoMovimiento.SelectedIndex = 0;
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnRegistrarClicked(object sender, EventArgs e)
        {
            if (PickerTipoMovimiento.SelectedIndex < 0 || string.IsNullOrWhiteSpace(EntryMonto.Text))
            {
                await ToastHelper.ShowError("Por favor complete todos los campos");
                return;
            }

            if (!decimal.TryParse(EntryMonto.Text, out decimal monto) || monto <= 0)
            {
                await ToastHelper.ShowError("El monto debe ser mayor a 0");
                return;
            }

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;
            BtnRegistrar.IsEnabled = false;

            try
            {
                var request = new MovimientoRequest
                {
                    NumeroCuenta = _numeroCuenta,
                    TipoMovimiento = PickerTipoMovimiento.SelectedItem.ToString(),
                    Monto = monto
                };

                var resultado = await _service.CrearMovimientoAsync(request);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Movimiento registrado exitosamente");
                    await Shell.Current.GoToAsync("..");
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al registrar movimiento: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
                BtnRegistrar.IsEnabled = true;
            }
        }
    }
}
