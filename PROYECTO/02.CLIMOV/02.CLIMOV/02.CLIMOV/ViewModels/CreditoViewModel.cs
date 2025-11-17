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
    public class CreditoViewModel : INotifyPropertyChanged
    {
        private readonly BanquitoSoapService _service;
        private ResultadoEvaluacion _resultadoEvaluacion;
        private ObservableCollection<CuotaResponse> _cuotas;
        private bool _isLoading;
        private bool _evaluacionRealizada;

        public ObservableCollection<CreditoResponse> Creditos { get; set; } = new ObservableCollection<CreditoResponse>();
        public event PropertyChangedEventHandler PropertyChanged;

        public ResultadoEvaluacion ResultadoEvaluacion
        {
            get => _resultadoEvaluacion;
            set { _resultadoEvaluacion = value; OnPropertyChanged(); }
        }

        public ObservableCollection<CuotaResponse> Cuotas
        {
            get => _cuotas;
            set { _cuotas = value; OnPropertyChanged(); }
        }

        public bool IsLoading
        {
            get => _isLoading;
            set { _isLoading = value; OnPropertyChanged(); }
        }

        public bool EvaluacionRealizada
        {
            get => _evaluacionRealizada;
            set { _evaluacionRealizada = value; OnPropertyChanged(); }
        }

        public ICommand CargarCuotasCommand { get; }

        public CreditoViewModel()
        {
            _service = new BanquitoSoapService();
            Cuotas = new ObservableCollection<CuotaResponse>();
            CargarCuotasCommand = new Command<long>(async (idCredito) => await CargarCuotas(idCredito));
        }

        public async Task<ResultadoEvaluacion> EvaluarCredito(SolicitudCredito solicitud)
        {
            IsLoading = true;
            EvaluacionRealizada = false;
            try
            {
                ResultadoEvaluacion = await _service.EvaluarCreditoAsync(solicitud);
                EvaluacionRealizada = true;
                return ResultadoEvaluacion;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al evaluar crédito: {ex.Message}");
                return null;
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<CreditoResponse> CrearCredito(SolicitudCredito solicitud)
        {
            IsLoading = true;
            try
            {
                var resultado = await _service.CrearCreditoAsync(solicitud);
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Crédito creado exitosamente");
                    return resultado;
                }
                return null;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al crear crédito: {ex.Message}");
                return null;
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<CreditoResponse> ObtenerCredito(long id)
        {
            IsLoading = true;
            try
            {
                return await _service.ObtenerCreditoAsync(id);
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al obtener crédito: {ex.Message}");
                return null;
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task CargarCuotas(long idCredito)
        {
            IsLoading = true;
            try
            {
                var cuotas = await _service.ListarPorCreditoAsync(idCredito);
                Cuotas.Clear();
                foreach (var cuota in cuotas)
                {
                    Cuotas.Add(cuota);
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar cuotas: {ex.Message}");
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<bool> PagarCuota(long idCuota)
        {
            var confirmar = await ToastHelper.ShowConfirmation("Confirmar", "¿Está seguro de marcar esta cuota como pagada?");
            if (!confirmar) return false;

            IsLoading = true;
            try
            {
                var resultado = await _service.ActualizarCuotaAsync(idCuota, new ActualizarCuotaRequest { Estado = "PAGADA" });
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess("Cuota marcada como pagada");
                    return true;
                }
                return false;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al pagar cuota: {ex.Message}");
                return false;
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task<bool> AnularCuota(long idCuota)
        {
            var confirmar = await ToastHelper.ShowConfirmation("Confirmar", "¿Está seguro de anular esta cuota?");
            if (!confirmar) return false;

            IsLoading = true;
            try
            {
                var resultado = await _service.AnularCuotaAsync(idCuota);
                if (!string.IsNullOrEmpty(resultado))
                {
                    await ToastHelper.ShowSuccess("Cuota anulada exitosamente");
                    return true;
                }
                return false;
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al anular cuota: {ex.Message}");
                return false;
            }
            finally
            {
                IsLoading = false;
            }
        }

        public async Task CargarCreditos()
        {
            IsLoading = true;
            try
            {
                var creditos = await _service.ListarCreditosAsync();
                Creditos.Clear();
                if (creditos != null)
                {
                    foreach (var credito in creditos)
                    {
                        Creditos.Add(credito);
                    }
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar créditos: {ex.Message}");
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
