using Microsoft.Maui.Controls;
using System;
using System.Globalization;

namespace _02.CLIMOV.Converters
{
    public class TipoMovimientoColorConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var tipo = value?.ToString();
            var param = parameter?.ToString();

            if (tipo == "DEPOSITO" || tipo == "DEPÓSITO")
            {
                return param switch
                {
                    "Start" => Color.FromArgb("#2E7D32"),
                    "End" => Color.FromArgb("#66BB6A"),
                    "Text" => Color.FromArgb("#2E7D32"),
                    _ => Color.FromArgb("#2E7D32")
                };
            }
            else // RETIRO
            {
                return param switch
                {
                    "Start" => Color.FromArgb("#C62828"),
                    "End" => Color.FromArgb("#EF5350"),
                    "Text" => Color.FromArgb("#C62828"),
                    _ => Color.FromArgb("#C62828")
                };
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
