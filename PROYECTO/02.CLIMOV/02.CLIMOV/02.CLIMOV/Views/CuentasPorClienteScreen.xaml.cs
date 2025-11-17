using _02.CLIMOV.ViewModels;

namespace _02.CLIMOV.Views
{
    [QueryProperty(nameof(Cedula), "cedula")]
    public partial class CuentasPorClienteScreen : ContentPage
    {
        private CuentaViewModel _viewModel;
        private string _cedula;

        public string Cedula
        {
            get => _cedula;
            set
            {
                _cedula = value;
                CargarCuentas();
            }
        }

        public CuentasPorClienteScreen()
        {
            InitializeComponent();
            _viewModel = (CuentaViewModel)BindingContext;
        }

        private async void CargarCuentas()
        {
            await _viewModel.CargarCuentasPorCliente(_cedula);
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
    }
}
