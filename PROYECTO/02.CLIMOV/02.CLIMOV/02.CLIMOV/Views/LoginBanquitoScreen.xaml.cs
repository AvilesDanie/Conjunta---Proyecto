namespace _02.CLIMOV.Views
{
    public partial class LoginBanquitoScreen : ContentPage
    {
        public LoginBanquitoScreen()
        {
            InitializeComponent();
        }

        private async void OnBackClicked(object sender, EventArgs e)
        {
            await Shell.Current.GoToAsync("..");
        }
    }
}
