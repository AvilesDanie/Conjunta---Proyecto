using _02.CLIMOV.Helpers;
using _02.CLIMOV.Models;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    [QueryProperty(nameof(Cedula), "cedula")]
    public partial class EditarClienteScreen : ContentPage
    {
        private readonly BanquitoSoapService _service;
        private string _cedula;

        public string Cedula
        {
            get => _cedula;
            set
            {
                _cedula = value;
                CargarCliente();
            }
        }

        public EditarClienteScreen()
        {
            InitializeComponent();
            _service = new BanquitoSoapService();
        }

        private async void CargarCliente()
        {
            try
            {
                var cliente = await _service.ObtenerClienteAsync(_cedula);
                if (cliente != null)
                {
                    EntryCedula.Text = cliente.Cedula;
                    EntryNombre.Text = cliente.Nombre;
                    PickerEstadoCivil.SelectedItem = cliente.EstadoCivil;
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar cliente: {ex.Message}");
            }
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnActualizarClicked(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(EntryNombre.Text) || PickerEstadoCivil.SelectedIndex < 0)
            {
                await ToastHelper.ShowError("Por favor complete todos los campos");
                return;
            }

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;
            BtnActualizar.IsEnabled = false;

            try
            {
                var request = new ClienteUpdateRequest
                {
                    Nombre = EntryNombre.Text,
                    EstadoCivil = PickerEstadoCivil.SelectedItem.ToString()
                };

                var resultado = await _service.ActualizarClienteAsync(_cedula, request);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Cliente actualizado exitosamente");
                    await Shell.Current.GoToAsync("..");
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al actualizar cliente: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
                BtnActualizar.IsEnabled = true;
            }
        }
    }
}
