using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using System.Windows.Input;
using _02.CLIMOV.Helpers;
using _02.CLIMOV.Models;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.ViewModels
{
    public class LoginBanquitoViewModel : INotifyPropertyChanged
    {
        private readonly BanquitoSoapService _service;
        private string _username;
        private string _password;
        private bool _isLoading;
        private bool _passwordVisible;

        public event PropertyChangedEventHandler PropertyChanged;

        public string Username
        {
            get => _username;
            set { _username = value; OnPropertyChanged(); }
        }

        public string Password
        {
            get => _password;
            set { _password = value; OnPropertyChanged(); }
        }

        public bool IsLoading
        {
            get => _isLoading;
            set { _isLoading = value; OnPropertyChanged(); }
        }

        public bool PasswordVisible
        {
            get => _passwordVisible;
            set { _passwordVisible = value; OnPropertyChanged(); }
        }

        public ICommand LoginCommand { get; }
        public ICommand TogglePasswordCommand { get; }

        public LoginBanquitoViewModel()
        {
            _service = new BanquitoSoapService();
            LoginCommand = new Command(async () => await Login());
            TogglePasswordCommand = new Command(() => PasswordVisible = !PasswordVisible);
        }

        private async Task Login()
        {
            if (string.IsNullOrWhiteSpace(Username) || string.IsNullOrWhiteSpace(Password))
            {
                await ToastHelper.ShowError("Por favor ingrese usuario y contraseña");
                return;
            }

            IsLoading = true;
            try
            {
                // Enviar contraseña en texto plano (el servidor hace el hash)
                System.Diagnostics.Debug.WriteLine($"[LOGIN] Usuario: {Username}");
                System.Diagnostics.Debug.WriteLine($"[LOGIN] Password length: {Password?.Length}");
                
                var result = await _service.LoginAsync(new LoginRequest
                {
                    Username = Username,
                    Password = Password  // Enviar contraseña en texto plano
                });

                if (result != null && result.Id > 0 && result.Activo)
                {
                    SessionManager.SetSession(result.Username, result.Rol, "BANQUITO");
                    await ToastHelper.ShowSuccess($"Bienvenido {result.Username}");
                    // Navegar reemplazando toda la pila
                    await Shell.Current.GoToAsync($"//app_selection/home_banquito");
                }
                else if (result != null && result.Id == 0)
                {
                    await ToastHelper.ShowError("Usuario no encontrado o contraseña incorrecta");
                }
                else if (result != null && !result.Activo)
                {
                    await ToastHelper.ShowError("Usuario inactivo - Contacte al administrador");
                }
                else
                {
                    await ToastHelper.ShowError("Error en la respuesta del servidor");
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al iniciar sesión: {ex.Message}");
            }
            finally
            {
                IsLoading = false;
            }
        }

        protected void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}
