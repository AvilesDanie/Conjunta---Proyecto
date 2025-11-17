using _02.CLIMOV.Helpers;

namespace _02.CLIMOV.Views
{
    public partial class HomeComercializadoraScreen : ContentPage
    {
        public HomeComercializadoraScreen()
        {
            InitializeComponent();
        }

        private async void OnElectrodomesticosClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("electrodomesticos");
        }

        private async void OnNuevoElectroClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("crear_electrodomestico");
        }

        private async void OnFacturarClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("facturar");
        }

        private async void OnFacturasClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("facturas");
        }

        private async void OnLogoutClicked(object sender, EventArgs e)
        {
            bool confirm = await DisplayAlert("Cerrar Sesión", "¿Está seguro de cerrar sesión?", "Sí", "No");
            if (confirm)
            {
                SessionManager.Username = null;
                SessionManager.Rol = null;
                SessionManager.AppContext = null;
                await Shell.Current.GoToAsync("//app_selection");
            }
        }
    }
}
