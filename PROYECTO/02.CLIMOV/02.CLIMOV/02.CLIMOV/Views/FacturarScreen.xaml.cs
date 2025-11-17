using _02.CLIMOV.Helpers;
using _02.CLIMOV.Models;
using _02.CLIMOV.Services;
using System.Collections.ObjectModel;

namespace _02.CLIMOV.Views
{
    public partial class FacturarScreen : ContentPage
    {
        private readonly ComercializadoraSoapService _service;
        private ObservableCollection<DetalleFactura> _productos;

        public FacturarScreen()
        {
            InitializeComponent();
            _service = new ComercializadoraSoapService();
            _productos = new ObservableCollection<DetalleFactura>();
            CollectionProductos.ItemsSource = _productos;
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }

        private async void OnAgregarProductoClicked(object sender, EventArgs e)
        {
            try
            {
                var electrodomesticos = await _service.ObtenerElectrodomesticosAsync();
                
                if (electrodomesticos == null || !electrodomesticos.Any())
                {
                    await ToastHelper.ShowError("No hay electrodomésticos disponibles");
                    return;
                }

                var nombres = electrodomesticos.Select(e => e.Nombre).ToArray();
                var seleccion = await DisplayActionSheet("Seleccione un producto", "Cancelar", null, nombres);

                if (seleccion != "Cancelar" && seleccion != null)
                {
                    var electroSeleccionado = electrodomesticos.FirstOrDefault(e => e.Nombre == seleccion);
                    
                    if (electroSeleccionado != null)
                    {
                        var cantidadStr = await DisplayPromptAsync("Cantidad", "Ingrese la cantidad:", keyboard: Keyboard.Numeric);
                        
                        if (!string.IsNullOrWhiteSpace(cantidadStr) && int.TryParse(cantidadStr, out int cantidad) && cantidad > 0)
                        {
                            _productos.Add(new DetalleFactura
                            {
                                IdElectrodomestico = (int)electroSeleccionado.IdElectrodomestico,
                                NombreElectrodomestico = electroSeleccionado.Nombre,
                                Cantidad = cantidad,
                                PrecioUnitario = electroSeleccionado.Precio
                            });

                            ActualizarTotal();
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error: {ex.Message}");
            }
        }

        private void OnEliminarProductoClicked(object sender, EventArgs e)
        {
            var button = (Button)sender;
            var detalle = button.CommandParameter as DetalleFactura;
            
            if (detalle != null)
            {
                _productos.Remove(detalle);
                ActualizarTotal();
            }
        }

        private void ActualizarTotal()
        {
            decimal total = _productos.Sum(p => p.PrecioUnitario * p.Cantidad);
            LabelTotal.Text = $"$ {total:N2}";
        }

        private async void OnFacturarClicked(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(EntryCedula.Text) || string.IsNullOrWhiteSpace(EntryNombreCliente.Text))
            {
                await ToastHelper.ShowError("Por favor complete los datos del cliente");
                return;
            }

            if (!_productos.Any())
            {
                await ToastHelper.ShowError("Debe agregar al menos un producto");
                return;
            }

            LoadingIndicator.IsRunning = true;
            LoadingIndicator.IsVisible = true;
            BtnFacturar.IsEnabled = false;

            try
            {
                var request = new FacturaRequest
                {
                    CedulaCliente = EntryCedula.Text,
                    NombreCliente = EntryNombreCliente.Text,
                    FormaPago = "EFECTIVO",
                    PlazoMeses = 0,
                    IdElectrodomestico = _productos.FirstOrDefault()?.IdElectrodomestico ?? 0,
                    Cantidad = _productos.FirstOrDefault()?.Cantidad ?? 1
                };

                var resultado = await _service.CrearFacturaAsync(request);
                
                if (resultado != null)
                {
                    await ToastHelper.ShowSuccess($"Factura generada exitosamente");
                    await Shell.Current.GoToAsync("..");
                }
            }
            catch (Exception ex)
            {
                await ToastHelper.ShowError($"Error al generar factura: {ex.Message}");
            }
            finally
            {
                LoadingIndicator.IsRunning = false;
                LoadingIndicator.IsVisible = false;
                BtnFacturar.IsEnabled = true;
            }
        }
    }
}
