using _02.CLIMOV.Helpers;

namespace _02.CLIMOV.Views
{
    public partial class HomeBanquitoScreen : ContentPage
    {
        public HomeBanquitoScreen()
        {
            InitializeComponent();
            LabelBienvenida.Text = $"Bienvenido, {SessionManager.Username}";
        }

        private async void OnClientesClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("clientes");
        }

        private async void OnNuevoClienteClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("crear_cliente");
        }

        private async void OnCuentasClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("todas_cuentas");
        }

        private async void OnCreditosClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("creditos");
        }

        private async void OnCuotasClicked(object sender, EventArgs e)
        {
            string idCredito = await DisplayPromptAsync("Consultar Cuotas", "Ingrese el ID del Crédito:");
            if (!string.IsNullOrWhiteSpace(idCredito))
            {
                await Shell.Current.GoToAsync($"cuotas?idCredito={idCredito}");
            }
        }

        private async void OnUsuariosClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("usuarios");
        }

        private async void OnLogoutClicked(object sender, EventArgs e)
        {
            bool confirm = await DisplayAlert("Cerrar Sesión", "¿Está seguro de cerrar sesión?", "Sí", "No");
            if (confirm)
            {
                SessionManager.Clear();
                await Shell.Current.GoToAsync("//splash");
            }
        }
    }
}
