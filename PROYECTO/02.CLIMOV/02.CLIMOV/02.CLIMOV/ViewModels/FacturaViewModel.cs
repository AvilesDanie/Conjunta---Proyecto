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
    public class FacturaViewModel : INotifyPropertyChanged
    {
        private readonly ComercializadoraSoapService _service;
        private ObservableCollection<FacturaResponse> _facturas;
        private bool _isLoading;

        public event PropertyChangedEventHandler PropertyChanged;

        public ObservableCollection<FacturaResponse> Facturas
        {
            get => _facturas;
            set { _facturas = value; OnPropertyChanged(); }
        }

        public bool IsLoading
        {
            get => _isLoading;
            set { _isLoading = value; OnPropertyChanged(); }
        }

        public ICommand CargarFacturasCommand { get; }

        public FacturaViewModel()
        {
            _service = new ComercializadoraSoapService();
            Facturas = new ObservableCollection<FacturaResponse>();
            CargarFacturasCommand = new Command(async () => await CargarFacturas());
        }

        public async Task CargarFacturas()
        {
            IsLoading = true;
            try
            {
                var facturas = await _service.ListarFacturasAsync();
                Facturas.Clear();
                foreach (var factura in facturas)
                {
                    Facturas.Add(factura);
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar facturas: {ex.Message}");
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<FacturaResponse> ObtenerFactura(long id)
        {
            IsLoading = true;
            try
            {
                return await _service.ObtenerFacturaAsync(id);
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al obtener factura: {ex.Message}");
                return null;
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<FacturaResponse> CrearFactura(FacturaRequest request)
        {
            IsLoading = true;
            try
            {
                var resultado = await _service.CrearFacturaAsync(request);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Factura creada exitosamente");
                    await CargarFacturas();
                    return resultado;
                }
                return null;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al crear factura: {ex.Message}");
                return null;
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
