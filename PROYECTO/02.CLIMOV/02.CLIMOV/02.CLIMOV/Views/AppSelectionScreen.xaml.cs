namespace _02.CLIMOV.Views
{
    public partial class AppSelectionScreen : ContentPage
    {
        public AppSelectionScreen()
        {
            InitializeComponent();
        }

        private async void OnBanquitoClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("login_banquito");
        }

        private async void OnComercializadoraClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("login_comercializadora");
        }
    }
}
