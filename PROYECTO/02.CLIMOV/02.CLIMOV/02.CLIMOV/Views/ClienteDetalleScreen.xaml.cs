using _02.CLIMOV.Helpers;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    [QueryProperty(nameof(Cedula), "cedula")]
    public partial class ClienteDetalleScreen : ContentPage
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

        public ClienteDetalleScreen()
        {
            InitializeComponent();
            _service = new BanquitoSoapService();
        }

        private async void CargarCliente()
        {
            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;

            try
            {
                var cliente = await _service.ObtenerClienteAsync(_cedula);
                if (cliente != null)
                {
                    LabelNombre.Text = cliente.Nombre;
                    LabelCedula.Text = cliente.Cedula;
                    LabelFechaNacimiento.Text = cliente.FechaNacimiento;
                    LabelEstadoCivil.Text = cliente.EstadoCivil;
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar cliente: {ex.Message}");
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

        private async void OnVerCuentasClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync($"cuentas_cliente?cedula={_cedula}");
        }

        private async void OnEditarClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync($"editar_cliente?cedula={_cedula}");
        }

        private async void OnEliminarClicked(object sender, EventArgs e)
        {
            bool confirm = await DisplayAlert("Confirmar", "¿Está seguro de eliminar este cliente?", "Sí", "No");
            if (!confirm) return;

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;

            try
            {
                var resultado = await _service.EliminarClienteAsync(_cedula);
                if (resultado)
                {
                    await ToastHelper.ShowSuccess("Cliente eliminado exitosamente");
                    await Shell.Current.GoToAsync("..");
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al eliminar cliente: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
            }
        }
    }
}
