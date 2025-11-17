<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="WebForm1.aspx.cs" Inherits="WS_DOTNET_SOAP_Comercializadora.WebForm1" %>


<!DOCTYPE html>
<html>
<head runat="server">
    <title>WS Comercializadora - Bienvenida</title>
</head>
<body>
    <form id="form1" runat="server">
        <h1>Bienvenido al Servicio SOAP de Comercializadora</h1>

        <p>
            Aquí puedes acceder al servicio SOAP y a las herramientas de prueba.
        </p>

        <ul>
            <li>
                <a href="ec.edu.monster.controller/UsuarioController.svc">Ver servicio UsuarioController SOAP / WSDL</a>
            </li>

            <li>
                <a href="ec.edu.monster.controller/ElectrodomesticoController.svc">Ver servicio ElectrodomesticoController SOAP / WSDL</a>
            </li>


            <li>
                <a href="ec.edu.monster.controller/FacturaController.svc">Ver servicio FacturaController SOAP / WSDL</a>
            </li>

            <li>
                <a href="ComercializadoraService.svc">Ver servicio Global SOAP / WSDL</a>
            </li>

            <li>
                <p> Pruebas con WcfTestClient en: "C:\Program Files\Microsoft Visual Studio\2022\Community\Common7\IDE\WcfTestClient.exe"</p>
            </li>
        </ul>

    </form>
</body>
</html>
