using _02.CLIMOV.ViewModels;

namespace _02.CLIMOV.Views
{
    public partial class CreditosScreen : ContentPage
    {
        private CreditoViewModel _viewModel;

        public CreditosScreen()
        {
            InitializeComponent();
            _viewModel = (CreditoViewModel)BindingContext;
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            await _viewModel.CargarCreditos();
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnVerCuotasClicked(object sender, EventArgs e)
        {
            var button = (Button)sender;
            var idCredito = button.CommandParameter?.ToString();
            await Shell.Current.GoToAsync($"cuotas?idCredito={idCredito}");
        }

        private async void OnEvaluarCreditoClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("evaluar_credito");
        }
    }
}
