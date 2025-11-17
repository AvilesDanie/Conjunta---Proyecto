using System;
using System.Runtime.Serialization;

namespace WS_DOTNET_SOAP_Comercializadora.ec.edu.monster.dto
{
    [DataContract]
    public class ElectroRequestDTO
    {
        [DataMember]
        public string Codigo { get; set; }

        [DataMember]
        public string Nombre { get; set; }

        [DataMember]
        public decimal PrecioVenta { get; set; }
    }

    [DataContract]
    public class ElectroResponseDTO
    {
        [DataMember]
        public long Id { get; set; }

        [DataMember]
        public string Codigo { get; set; }

        [DataMember]
        public string Nombre { get; set; }

        [DataMember]
        public decimal PrecioVenta { get; set; }
    }
}
