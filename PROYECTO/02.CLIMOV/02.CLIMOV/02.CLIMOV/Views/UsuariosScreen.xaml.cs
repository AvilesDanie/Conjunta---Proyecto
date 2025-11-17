using _02.CLIMOV.Helpers;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    public partial class UsuariosScreen : ContentPage
    {
        private readonly BanquitoSoapService _service;

        public UsuariosScreen()
        {
            InitializeComponent();
            _service = new BanquitoSoapService();
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            await CargarUsuarios();
        }

        private async Task CargarUsuarios()
        {
            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;

            try
            {
                var usuarios = await _service.ObtenerUsuariosAsync();
                CollectionUsuarios.ItemsSource = usuarios;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar usuarios: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
            }
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }
    }
}
