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
    public class ElectrodomesticoViewModel : INotifyPropertyChanged
    {
        private readonly ComercializadoraSoapService _service;
        private ObservableCollection<ElectrodomesticoResponse> _electrodomesticos;
        private bool _isLoading;

        public event PropertyChangedEventHandler PropertyChanged;

        public ObservableCollection<ElectrodomesticoResponse> Electrodomesticos
        {
            get => _electrodomesticos;
            set { _electrodomesticos = value; OnPropertyChanged(); }
        }

        public bool IsLoading
        {
            get => _isLoading;
            set { _isLoading = value; OnPropertyChanged(); }
        }

        public ICommand CargarElectrodomesticosCommand { get; }

        public ElectrodomesticoViewModel()
        {
            _service = new ComercializadoraSoapService();
            Electrodomesticos = new ObservableCollection<ElectrodomesticoResponse>();
            CargarElectrodomesticosCommand = new Command(async () => await CargarElectrodomesticos());
        }

        public async Task CargarElectrodomesticos()
        {
            IsLoading = true;
            try
            {
                var electrodomesticos = await _service.ListarElectrodomesticosAsync();
                Electrodomesticos.Clear();
                foreach (var electro in electrodomesticos)
                {
                    Electrodomesticos.Add(electro);
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar electrodomésticos: {ex.Message}");
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<bool> CrearElectrodomestico(ElectrodomesticoRequest request)
        {
            IsLoading = true;
            try
            {
                var resultado = await _service.CrearElectrodomesticoAsync(request);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Electrodoméstico creado exitosamente");
                    await CargarElectrodomesticos();
                    return true;
                }
                return false;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al crear electrodoméstico: {ex.Message}");
                return false;
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
