using _02.CLIMOV.Helpers;
using _02.CLIMOV.Services;

namespace _02.CLIMOV.Views
{
    [QueryProperty(nameof(IdFactura), "idFactura")]
    public partial class FacturaDetalleScreen : ContentPage
    {
        private readonly ComercializadoraSoapService _service;
        private string _idFactura;

        public string IdFactura
        {
            get => _idFactura;
            set
            {
                _idFactura = value;
                CargarFactura();
            }
        }

        public FacturaDetalleScreen()
        {
            InitializeComponent();
            _service = new ComercializadoraSoapService();
        }

        private async void CargarFactura()
        {
            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;

            try
            {
                if (int.TryParse(_idFactura, out int id))
                {
                    var factura = await _service.ObtenerFacturaAsync(id);
                    
                    if (factura != null)
                    {
                        LabelNumeroFactura.Text = factura.NumeroFactura;
                        LabelFecha.Text = factura.Fecha;
                        LabelCedula.Text = factura.CedulaCliente;
                        LabelNombreCliente.Text = factura.NombreCliente;
                        LabelTotal.Text = $"$ {factura.Total:N2}";

                        CollectionDetalles.ItemsSource = factura.Detalles;
                    }
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al cargar factura: {ex.Message}");
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
