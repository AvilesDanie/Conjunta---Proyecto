using Microsoft.Maui.Controls;
using System;
using System.Globalization;

namespace _02.CLIMOV.Converters
{
    public class EstadoCuotaToBoolConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var estado = value?.ToString();
            var expectedEstado = parameter?.ToString();

            return estado == expectedEstado;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
