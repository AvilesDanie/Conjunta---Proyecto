namespace _02.CLIMOV.Views
{
    public partial class SplashScreen : ContentPage
    {
        public SplashScreen()
        {
            InitializeComponent();
            NavigateToAppSelection();
        }

        private async void NavigateToAppSelection()
        {
            await Task.Delay(2000); // 2 segundos
            await Shell.Current.GoToAsync("//app_selection");
        }
    }
}
