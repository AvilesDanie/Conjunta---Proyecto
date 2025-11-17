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
    public class ClienteViewModel : INotifyPropertyChanged
    {
        private readonly BanquitoSoapService _service;
        private ObservableCollection<ClienteResponse> _clientes;
        private ObservableCollection<ClienteResponse> _clientesFiltrados;
        private string _searchText;
        private bool _isLoading;
        private ClienteOnlyResponse _clienteSeleccionado;

        public event PropertyChangedEventHandler PropertyChanged;

        public ObservableCollection<ClienteResponse> Clientes
        {
            get => _clientes;
            set { _clientes = value; OnPropertyChanged(); }
        }

        public ObservableCollection<ClienteResponse> ClientesFiltrados
        {
            get => _clientesFiltrados;
            set { _clientesFiltrados = value; OnPropertyChanged(); }
        }

        public string SearchText
        {
            get => _searchText;
            set
            {
                _searchText = value;
                OnPropertyChanged();
                FiltrarClientes();
            }
        }

        public bool IsLoading
        {
            get => _isLoading;
            set { _isLoading = value; OnPropertyChanged(); }
        }

        public ClienteOnlyResponse ClienteSeleccionado
        {
            get => _clienteSeleccionado;
            set { _clienteSeleccionado = value; OnPropertyChanged(); }
        }

        public ICommand CargarClientesCommand { get; }
        public ICommand EliminarClienteCommand { get; }

        public ClienteViewModel()
        {
            _service = new BanquitoSoapService();
            Clientes = new ObservableCollection<ClienteResponse>();
            ClientesFiltrados = new ObservableCollection<ClienteResponse>();
            CargarClientesCommand = new Command(async () => await CargarClientes());
            EliminarClienteCommand = new Command<string>(async (cedula) => await EliminarCliente(cedula));
        }

        public async Task CargarClientes()
        {
            IsLoading = true;
            try
            {
                var clientes = await _service.ListarClientesAsync();
                Clientes.Clear();
                ClientesFiltrados.Clear();

                foreach (var cliente in clientes)
                {
                    Clientes.Add(cliente);
                    ClientesFiltrados.Add(cliente);
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar clientes: {ex.Message}");
            }
            finally
            {
                IsLoading = false;
            }
        }

        private void FiltrarClientes()
        {
            ClientesFiltrados.Clear();

            if (string.IsNullOrWhiteSpace(SearchText))
            {
                foreach (var cliente in Clientes)
                {
                    ClientesFiltrados.Add(cliente);
                }
            }
            else
            {
                var filtro = SearchText.ToLower();
                foreach (var cliente in Clientes)
                {
                    if (cliente.Nombre.ToLower().Contains(filtro) ||
                        cliente.Cedula.Contains(filtro))
                    {
                        ClientesFiltrados.Add(cliente);
                    }
                }
            }
        }

        public async Task<ClienteOnlyResponse> ObtenerCliente(string cedula)
        {
            IsLoading = true;
            try
            {
                return await _service.ObtenerClienteAsync(cedula);
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al obtener cliente: {ex.Message}");
                return null;
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<bool> CrearCliente(ClienteRequest request)
        {
            IsLoading = true;
            try
            {
                var resultado = await _service.CrearClienteAsync(request);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Cliente creado exitosamente");
                    await CargarClientes();
                    return true;
                }
                return false;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al crear cliente: {ex.Message}");
                return false;
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<bool> ActualizarCliente(string cedula, ClienteUpdateRequest request)
        {
            IsLoading = true;
            try
            {
                var resultado = await _service.ActualizarClienteAsync(cedula, request);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Cliente actualizado exitosamente");
                    await CargarClientes();
                    return true;
                }
                return false;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al actualizar cliente: {ex.Message}");
                return false;
            }
            finally
            {
                IsLoading = false;
            }
        }

        private async Task EliminarCliente(string cedula)
        {
            var confirmar = await ToastHelper.ShowConfirmation("Confirmar", "¿Está seguro de eliminar este cliente?");
            if (!confirmar) return;

            IsLoading = true;
            try
            {
                var resultado = await _service.EliminarClienteAsync(cedula);
                if (resultado)
                {
                    await ToastHelper.ShowSuccess("Cliente eliminado exitosamente");
                    await CargarClientes();
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al eliminar cliente: {ex.Message}");
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
