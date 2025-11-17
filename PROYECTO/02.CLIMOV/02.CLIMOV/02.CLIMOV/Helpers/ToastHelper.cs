using System.Threading.Tasks;

namespace _02.CLIMOV.Helpers
{
    public static class ToastHelper
    {
        public static Task ShowToast(string message)
        {
            if (Application.Current?.MainPage != null)
            {
                return Application.Current.MainPage.DisplayAlert("Información", message, "OK");
            }
            return Task.CompletedTask;
        }

        public static Task ShowError(string message)
        {
            if (Application.Current?.MainPage != null)
            {
                return Application.Current.MainPage.DisplayAlert("Error", message, "OK");
            }
            return Task.CompletedTask;
        }

        public static Task ShowSuccess(string message)
        {
            if (Application.Current?.MainPage != null)
            {
                return Application.Current.MainPage.DisplayAlert("Éxito", message, "OK");
            }
            return Task.CompletedTask;
        }

        public static Task<bool> ShowConfirmation(string title, string message)
        {
            if (Application.Current?.MainPage != null)
            {
                return Application.Current.MainPage.DisplayAlert(title, message, "Sí", "No");
            }
            return Task.FromResult(false);
        }
    }
}
