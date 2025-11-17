using _02.CLIMOV.ViewModels;

namespace _02.CLIMOV.Views
{
    public partial class FacturasScreen : ContentPage
    {
        private FacturaViewModel _viewModel;

        public FacturasScreen()
        {
            InitializeComponent();
            _viewModel = (FacturaViewModel)BindingContext;
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            await _viewModel.CargarFacturas();
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnFacturaSelected(object sender, EventArgs e)
        {
            var tapGesture = (TapGestureRecognizer)sender;
            var idFactura = tapGesture.CommandParameter?.ToString();
            await Shell.Current.GoToAsync($"factura_detalle?idFactura={idFactura}");
        }
    }
}
