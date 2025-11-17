using System;
using System.Security.Cryptography;
using System.Text;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.utils
{
    public static class PasswordUtil
    {
        public static string HashPassword(string plain)
        {
            if (plain == null) return null;

            using (var sha = SHA256.Create())
            {
                byte[] bytes = Encoding.UTF8.GetBytes(plain);
                byte[] hashBytes = sha.ComputeHash(bytes);

                var sb = new StringBuilder(hashBytes.Length * 2);
                foreach (byte b in hashBytes)
                {
                    sb.AppendFormat("{0:x2}", b); // hex minúsculas
                }
                return sb.ToString();
            }
        }

        public static bool Matches(string plain, string hash)
        {
            if (string.IsNullOrEmpty(plain) || string.IsNullOrEmpty(hash))
                return false;

            string calculated = HashPassword(plain);
            return string.Equals(calculated, hash, StringComparison.OrdinalIgnoreCase);
        }
    }
}
