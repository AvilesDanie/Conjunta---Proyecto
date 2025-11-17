using _02.CLIMOV.Helpers;
using _02.CLIMOV.Models;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    public partial class CrearCuentaScreen : ContentPage
    {
        private readonly BanquitoSoapService _service;

        public CrearCuentaScreen()
        {
            InitializeComponent();
            _service = new BanquitoSoapService();
            PickerTipoCuenta.SelectedIndex = 0;
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnCrearClicked(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(EntryCedula.Text) || PickerTipoCuenta.SelectedIndex < 0)
            {
                await ToastHelper.ShowError("Por favor complete todos los campos obligatorios");
                return;
            }

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;
            BtnCrear.IsEnabled = false;

            try
            {
                decimal saldoInicial = 0;
                if (!string.IsNullOrWhiteSpace(EntrySaldoInicial.Text))
                {
                    if (!decimal.TryParse(EntrySaldoInicial.Text, out saldoInicial))
                    {
                        await ToastHelper.ShowError("El saldo inicial debe ser un número válido");
                        return;
                    }
                }

                var request = new CuentaRequest
                {
                    CedulaCliente = EntryCedula.Text,
                    TipoCuenta = PickerTipoCuenta.SelectedItem.ToString(),
                    SaldoInicial = saldoInicial
                };

                var resultado = await _service.CrearCuentaAsync(request);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess($"Cuenta creada exitosamente: {resultado.NumeroCuenta}");
                    await Shell.Current.GoToAsync("..");
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al crear cuenta: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
                BtnCrear.IsEnabled = true;
            }
        }
    }
}
