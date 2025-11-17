using _02.CLIMOV.ViewModels;

namespace _02.CLIMOV.Views
{
    public partial class ClientesScreen : ContentPage
    {
        private ClienteViewModel _viewModel;

        public ClientesScreen()
        {
            InitializeComponent();
            _viewModel = (ClienteViewModel)BindingContext;
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            await _viewModel.CargarClientes();
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnClienteSelected(object sender, EventArgs e)
        {
            var tapGesture = (TapGestureRecognizer)sender;
            var cedula = tapGesture.CommandParameter as string;
            await Shell.Current.GoToAsync($"cliente_detalle?cedula={cedula}");
        }

        private async void OnNuevoClienteClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("crear_cliente");
        }
    }
}
