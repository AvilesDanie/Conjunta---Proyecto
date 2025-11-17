using _02.CLIMOV.Helpers;
using _02.CLIMOV.Services;
using _02.CLIMOV.ViewModels;

namespace _02.CLIMOV.Views
{
    public partial class TodasCuentasScreen : ContentPage
    {
        private CuentaViewModel _viewModel;
        private readonly BanquitoSoapService _service;

        public TodasCuentasScreen()
        {
            InitializeComponent();
            _viewModel = (CuentaViewModel)BindingContext;
            _service = new BanquitoSoapService();
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            await _viewModel.CargarTodasLasCuentas();
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnVerMovimientosClicked(object sender, EventArgs e)
        {
            var button = (Button)sender;
            var numeroCuenta = button.CommandParameter as string;
            await Shell.Current.GoToAsync($"movimientos?numeroCuenta={numeroCuenta}");
        }

        private async void OnEliminarClicked(object sender, EventArgs e)
        {
            var button = (Button)sender;
            var numeroCuenta = button.CommandParameter as string;

            bool confirm = await DisplayAlert("Confirmar", $"¿Está seguro de eliminar la cuenta {numeroCuenta}?", "Sí", "No");
            if (!confirm) return;

            try
            {
                var resultado = await _service.EliminarCuentaAsync(numeroCuenta);
                if (resultado)
                {
                    await ToastHelper.ShowSuccess("Cuenta eliminada exitosamente");
                    await _viewModel.CargarTodasLasCuentas();
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al eliminar cuenta: {ex.Message}");
            }
        }

        private async void OnNuevaCuentaClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("crear_cuenta");
        }
    }
}
