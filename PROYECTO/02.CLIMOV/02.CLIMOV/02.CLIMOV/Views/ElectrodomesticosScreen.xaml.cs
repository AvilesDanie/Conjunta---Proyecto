using _02.CLIMOV.Helpers;
using _02.CLIMOV.Services;
using _02.CLIMOV.ViewModels;

namespace _02.CLIMOV.Views
{
    public partial class ElectrodomesticosScreen : ContentPage
    {
        private ElectrodomesticoViewModel _viewModel;
        private readonly ComercializadoraSoapService _service;

        public ElectrodomesticosScreen()
        {
            InitializeComponent();
            _viewModel = (ElectrodomesticoViewModel)BindingContext;
            _service = new ComercializadoraSoapService();
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            await _viewModel.CargarElectrodomesticos();
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnNuevoClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("crear_electrodomestico");
        }

        private async void OnEditarClicked(object sender, EventArgs e)
        {
            var button = (Button)sender;
            var id = button.CommandParameter?.ToString();
            await Shell.Current.GoToAsync($"editar_electrodomestico?id={id}");
        }

        private async void OnEliminarClicked(object sender, EventArgs e)
        {
            var button = (Button)sender;
            var id = Convert.ToInt32(button.CommandParameter);

            bool confirm = await DisplayAlert("Confirmar", "¿Está seguro de eliminar este electrodoméstico?", "Sí", "No");
            if (!confirm) return;

            try
            {
                var resultado = await _service.EliminarElectrodomesticoAsync(id);
                if (resultado)
                {
                    await ToastHelper.ShowSuccess("Electrodoméstico eliminado exitosamente");
                    await _viewModel.CargarElectrodomesticos();
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al eliminar: {ex.Message}");
            }
        }
    }
}
