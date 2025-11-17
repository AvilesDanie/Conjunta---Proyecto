using _02.CLIMOV.Helpers;
using _02.CLIMOV.Models;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    public partial class LoginComercializadoraScreen : ContentPage
    {
        private readonly ComercializadoraSoapService _service;
        public bool IsPasswordHidden { get; set; } = true;

        public LoginComercializadoraScreen()
        {
            InitializeComponent();
            _service = new ComercializadoraSoapService();
            BindingContext = this;
        }

        private async void OnLoginClicked(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(EntryUsuario.Text) || string.IsNullOrWhiteSpace(EntryPassword.Text))
            {
                await ToastHelper.ShowError("Por favor ingrese usuario y contraseña");
                return;
            }

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;
            BtnLogin.IsEnabled = false;

            try
            {
                var request = new UsuarioComercializadoraRequest
                {
                    Username = EntryUsuario.Text,
                    Password = EntryPassword.Text
                };

                var usuario = await _service.LoginAsync(request);
                
                if (usuario != null)
                {
                    SessionManager.Username = usuario.Username;
                    SessionManager.Rol = usuario.Rol;
                    SessionManager.AppContext = "COMERCIALIZADORA";

                    await ToastHelper.ShowSuccess($"Bienvenido {usuario.NombreUsuario}");
                    await Shell.Current.GoToAsync("//home_comercializadora");
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al iniciar sesión: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
                BtnLogin.IsEnabled = true;
            }
        }

        private void OnTogglePasswordClicked(object sender, EventArgs e)
        {
            IsPasswordHidden = !IsPasswordHidden;
            OnPropertyChanged(nameof(IsPasswordHidden));
        }

        private async void OnVolverClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }
    }
}
