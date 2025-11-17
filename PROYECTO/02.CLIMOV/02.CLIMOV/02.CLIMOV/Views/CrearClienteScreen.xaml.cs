using _02.CLIMOV.Helpers;
using _02.CLIMOV.Models;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    public partial class CrearClienteScreen : ContentPage
    {
        private readonly BanquitoSoapService _service;

        public CrearClienteScreen()
        {
            InitializeComponent();
            _service = new BanquitoSoapService();
            PickerTipoCuenta.SelectedIndex = 0;
            PickerEstadoCivil.SelectedIndex = 0;
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnGuardarClicked(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(EntryCedula.Text) || 
                string.IsNullOrWhiteSpace(EntryNombre.Text) ||
                PickerEstadoCivil.SelectedIndex < 0 ||
                PickerTipoCuenta.SelectedIndex < 0)
            {
                await ToastHelper.ShowError("Por favor complete todos los campos obligatorios");
                return;
            }

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;
            BtnGuardar.IsEnabled = false;

            try
            {
                var request = new ClienteRequest
                {
                    Cedula = EntryCedula.Text,
                    Nombre = EntryNombre.Text,
                    FechaNacimiento = DatePickerNacimiento.Date.ToString("yyyy-MM-dd"),
                    EstadoCivil = PickerEstadoCivil.SelectedItem.ToString(),
                    TipoCuentaInicial = PickerTipoCuenta.SelectedItem.ToString()
                };

                var resultado = await _service.CrearClienteAsync(request);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Cliente creado exitosamente");
                    await Shell.Current.GoToAsync("..");
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al crear cliente: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
                BtnGuardar.IsEnabled = true;
            }
        }
    }
}
