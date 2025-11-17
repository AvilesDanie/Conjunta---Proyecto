using Microsoft.Maui.Controls;
using System;
using System.Globalization;

namespace _02.CLIMOV.Converters
{
    public class TipoMovimientoIconConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var tipo = value?.ToString();
            
            if (tipo == "DEPOSITO" || tipo == "DEPÓSITO")
                return "⬇️";
            else if (tipo == "RETIRO")
                return "⬆️";
            
            return "💵";
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
