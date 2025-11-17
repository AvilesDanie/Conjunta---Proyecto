namespace _02.CLIMOV.Helpers
{
    public static class SessionManager
    {
        private static string _username;
        private static string _rol;
        private static string _appContext; // "BANQUITO" o "COMERCIALIZADORA"

        public static string Username
        {
            get => _username;
            set => _username = value;
        }

        public static string Rol
        {
            get => _rol;
            set => _rol = value;
        }

        public static string AppContext
        {
            get => _appContext;
            set => _appContext = value;
        }

        public static bool IsLoggedIn => !string.IsNullOrEmpty(_username);

        public static void Clear()
        {
            _username = null;
            _rol = null;
            _appContext = null;
        }

        public static void SetSession(string username, string rol, string appContext)
        {
            Username = username;
            Rol = rol;
            AppContext = appContext;
        }
    }
}
