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
    public class CuentaViewModel : INotifyPropertyChanged
    {
        private readonly BanquitoSoapService _service;
        private ObservableCollection<CuentaResponse> _cuentas;
        private bool _isLoading;

        public event PropertyChangedEventHandler PropertyChanged;

        public ObservableCollection<CuentaResponse> Cuentas
        {
            get => _cuentas;
            set { _cuentas = value; OnPropertyChanged(); }
        }

        public bool IsLoading
        {
            get => _isLoading;
            set { _isLoading = value; OnPropertyChanged(); }
        }

        public ICommand CargarCuentasCommand { get; }

        public CuentaViewModel()
        {
            _service = new BanquitoSoapService();
            Cuentas = new ObservableCollection<CuentaResponse>();
            CargarCuentasCommand = new Command(async () => await CargarTodasCuentas());
        }

        public async Task CargarTodasCuentas()
        {
            IsLoading = true;
            try
            {
                var cuentas = await _service.ListarCuentasAsync();
                Cuentas.Clear();
                foreach (var cuenta in cuentas)
                {
                    Cuentas.Add(cuenta);
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar cuentas: {ex.Message}");
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task CargarCuentasPorCliente(string cedula)
        {
            IsLoading = true;
            try
            {
                var cuentas = await _service.ListarCuentasPorClienteAsync(cedula);
                Cuentas.Clear();
                foreach (var cuenta in cuentas)
                {
                    Cuentas.Add(cuenta);
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar cuentas: {ex.Message}");
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<CuentaResponse> ObtenerCuenta(string numCuenta)
        {
            IsLoading = true;
            try
            {
                return await _service.ObtenerCuentaAsync(numCuenta);
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al obtener cuenta: {ex.Message}");
                return null;
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<bool> CrearCuenta(CuentaRequest request)
        {
            IsLoading = true;
            try
            {
                var resultado = await _service.CrearCuentaAsync(request);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Cuenta creada exitosamente");
                    return true;
                }
                return false;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al crear cuenta: {ex.Message}");
                return false;
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task CargarTodasLasCuentas()
        {
            IsLoading = true;
            try
            {
                var cuentas = await _service.ListarCuentasAsync();
                Cuentas.Clear();
                if (cuentas != null)
                {
                    foreach (var cuenta in cuentas)
                    {
                        Cuentas.Add(cuenta);
                    }
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar cuentas: {ex.Message}");
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
