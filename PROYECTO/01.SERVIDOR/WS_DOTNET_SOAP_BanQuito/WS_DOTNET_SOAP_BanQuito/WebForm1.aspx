<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="WebForm1.aspx.cs" Inherits="WS_DOTNET_SOAP_BanQuito.WebForm1" %>


<!DOCTYPE html>
<html>
<head runat="server">
    <title>WS BanQuito - Bienvenida</title>
</head>
<body>
    <form id="form1" runat="server">
        <h1>Bienvenido al Servicio SOAP de BanQuito</h1>

        <p>
            Aquí puedes acceder al servicio SOAP y a las herramientas de prueba.
        </p>

        <ul>
            
            <li>
                <a href="ec.edu.monster.controller/UsuarioController.svc">Ver servicio UsuarioController SOAP / WSDL</a>
            </li>

            <li>
                <a href="ec.edu.monster.controller/ClienteController.svc">Ver servicio ClienteController SOAP / WSDL</a>
            </li>


            <li>
                <a href="ec.edu.monster.controller/CuentaController.svc">Ver servicio CuentaController SOAP / WSDL</a>
            </li>

            <li>
                <a href="ec.edu.monster.controller/MovimientoController.svc">Ver servicio MovimientoController SOAP / WSDL</a>
            </li>

            <li>
                <a href="ec.edu.monster.controller/CreditoController.svc">Ver servicio CreditoController SOAP / WSDL</a>
            </li>

            <li>
                <a href="ec.edu.monster.controller/CuotaController.svc">Ver servicio CuotaController SOAP / WSDL</a>
            </li>

            <li>
                <a href="BanQuitoService.svc">Ver servicio Global SOAP / WSDL</a>
            </li>

            <li>
                <p> Pruebas con WcfTestClient en: "C:\Program Files\Microsoft Visual Studio\2022\Community\Common7\IDE\WcfTestClient.exe"</p>
            </li>
        </ul>

    </form>
</body>
</html>
