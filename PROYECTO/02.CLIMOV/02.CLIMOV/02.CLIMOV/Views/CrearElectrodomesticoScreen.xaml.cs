using _02.CLIMOV.Helpers;
using _02.CLIMOV.Models;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    public partial class CrearElectrodomesticoScreen : ContentPage
    {
        private readonly ComercializadoraSoapService _service;

        public CrearElectrodomesticoScreen()
        {
            InitializeComponent();
            _service = new ComercializadoraSoapService();
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnGuardarClicked(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(EntryNombre.Text) ||
                string.IsNullOrWhiteSpace(EditorDescripcion.Text) ||
                string.IsNullOrWhiteSpace(EntryPrecio.Text) ||
                string.IsNullOrWhiteSpace(EntryStock.Text))
            {
                await ToastHelper.ShowError("Por favor complete todos los campos");
                return;
            }

            if (!decimal.TryParse(EntryPrecio.Text, out decimal precio) || precio <= 0)
            {
                await ToastHelper.ShowError("El precio debe ser mayor a 0");
                return;
            }

            if (!int.TryParse(EntryStock.Text, out int stock) || stock < 0)
            {
                await ToastHelper.ShowError("El stock debe ser mayor o igual a 0");
                return;
            }

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;
            BtnGuardar.IsEnabled = false;

            try
            {
                var request = new ElectrodomesticoRequest
                {
                    Codigo = EntryNombre.Text?.Substring(0, Math.Min(5, EntryNombre.Text.Length)),
                    Nombre = EntryNombre.Text,
                    PrecioVenta = precio
                };

                var resultado = await _service.CrearElectrodomesticoAsync(request);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Electrodoméstico creado exitosamente");
                    await Shell.Current.GoToAsync("..");
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al crear electrodoméstico: {ex.Message}");
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
