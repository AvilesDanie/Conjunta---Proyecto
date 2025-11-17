using _02.CLIMOV.Helpers;
using _02.CLIMOV.Models;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    public partial class EvaluarCreditoScreen : ContentPage
    {
        private readonly BanquitoSoapService _service;
        private ResultadoEvaluacion _resultado;

        public EvaluarCreditoScreen()
        {
            InitializeComponent();
            _service = new BanquitoSoapService();
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnEvaluarClicked(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(EntryNumeroCuenta.Text) ||
                string.IsNullOrWhiteSpace(EntryMonto.Text) ||
                string.IsNullOrWhiteSpace(EntryPlazo.Text))
            {
                await ToastHelper.ShowError("Por favor complete todos los campos");
                return;
            }

            if (!decimal.TryParse(EntryMonto.Text, out decimal monto) || monto <= 0)
            {
                await ToastHelper.ShowError("El monto debe ser mayor a 0");
                return;
            }

            if (!int.TryParse(EntryPlazo.Text, out int plazo) || plazo <= 0)
            {
                await ToastHelper.ShowError("El plazo debe ser mayor a 0");
                return;
            }

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;
            BtnEvaluar.IsEnabled = false;

            try
            {
                var solicitud = new SolicitudCredito
                {
                    NumeroCuenta = EntryNumeroCuenta.Text,
                    Monto = monto,
                    PlazoMeses = plazo
                };

                _resultado = await _service.EvaluarCreditoAsync(solicitud);
                
                if (_resultado != null)
                {
                    MostrarResultado();
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al evaluar crédito: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
                BtnEvaluar.IsEnabled = true;
            }
        }

        private void MostrarResultado()
        {
            FrameResultado.IsVisible = true;

            if (_resultado.Aprobado)
            {
                FrameAprobado.IsVisible = true;
                FrameRechazado.IsVisible = false;

                LabelTasa.Text = $"{_resultado.TasaInteres}%";
                LabelCuotaMensual.Text = $"$ {_resultado.CuotaMensual:N2}";
                LabelTotalPagar.Text = $"$ {_resultado.TotalPagar:N2}";
            }
            else
            {
                FrameAprobado.IsVisible = false;
                FrameRechazado.IsVisible = true;

                LabelMotivoRechazo.Text = _resultado.Mensaje ?? "No cumple con los requisitos mínimos";
            }
        }

        private async void OnVerTablaClicked(object sender, EventArgs e)
        {
            if (_resultado != null && _resultado.Aprobado)
            {
                await Shell.Current.GoToAsync($"tabla_amortizacion?numeroCuenta={EntryNumeroCuenta.Text}&monto={EntryMonto.Text}&plazo={EntryPlazo.Text}&tasa={_resultado.TasaInteres}");
            }
        }
    }
}
