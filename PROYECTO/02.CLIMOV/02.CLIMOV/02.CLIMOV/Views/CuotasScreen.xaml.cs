using _02.CLIMOV.Helpers;
using _02.CLIMOV.Models;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    [QueryProperty(nameof(IdCredito), "idCredito")]
    public partial class CuotasScreen : ContentPage
    {
        private readonly BanquitoSoapService _service;
        private string _idCredito;

        public string IdCredito
        {
            get => _idCredito;
            set
            {
                _idCredito = value;
                _ = CargarCuotas(); // No se puede usar await en setters
            }
        }

        public CuotasScreen()
        {
            InitializeComponent();
            _service = new BanquitoSoapService();
        }

        // 🔥 Cambiado de async void → async Task
        private async Task CargarCuotas()
        {
            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;

            try
            {
                if (int.TryParse(_idCredito, out int id))
                {
                    var cuotas = await _service.ObtenerCuotasPorCreditoAsync(id);
                    CollectionCuotas.ItemsSource = cuotas;
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar cuotas: {ex.Message}");
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

        private async void OnPagarClicked(object sender, EventArgs e)
        {
            var button = (Button)sender;
            var idCuota = Convert.ToInt32(button.CommandParameter);

            bool confirm = await DisplayAlert("Confirmar", "¿Desea registrar el pago de esta cuota?", "Sí", "No");
            if (!confirm) return;

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;

            try
            {
                var request = new ActualizarCuotaRequest
                {
                    IdCuota = idCuota,
                    Estado = "PAGADA"
                };

                var resultado = await _service.ActualizarCuotaAsync(idCuota, request);

                _ = ToastHelper.ShowSuccess("Cuota pagada exitosamente");

                await CargarCuotas(); // ahora sí funciona
            }
            catch (Exception ex)
            {
                _ = ToastHelper.ShowError($"Error al pagar cuota: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
            }
        }

        private async void OnCancelarClicked(object sender, EventArgs e)
        {
            var button = (Button)sender;
            var idCuota = Convert.ToInt32(button.CommandParameter);

            bool confirm = await DisplayAlert("Confirmar", "¿Desea cancelar esta cuota?", "Sí", "No");
            if (!confirm) return;

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;

            try
            {
                var request = new ActualizarCuotaRequest
                {
                    IdCuota = idCuota,
                    Estado = "CANCELADA"
                };

                var resultado = await _service.ActualizarCuotaAsync(idCuota, request);

                _ = ToastHelper.ShowSuccess("Cuota cancelada");

                await CargarCuotas(); // ahora sí funciona
            }
            catch (Exception ex)
            {
                _ = ToastHelper.ShowError($"Error al cancelar cuota: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
            }
        }
    }
}
