<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>ElectroQuito - Inicio</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', sans-serif;
            background: #f9fafb;
            display: flex;
            flex-direction: column;
        }

        .app-bar {
            background: #f97316;
            color: #ffffff;
            padding: 14px 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            box-shadow: 0 4px 10px rgba(0,0,0,0.25);
        }

        .app-bar-left {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .avatar {
            width: 44px;
            height: 44px;
            border-radius: 50%;
            background: #fed7aa;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 700;
            font-size: 22px;
            color: #c2410c;
        }

        .title-block {
            display: flex;
            flex-direction: column;
        }

        .title-main {
            font-size: 20px;
            font-weight: 700;
            margin: 0;
        }

        .title-sub {
            font-size: 13px;
            margin: 2px 0 0;
            opacity: 0.95;
        }

        .logout-btn {
            border: none;
            background: rgba(255,255,255,0.15);
            color: #ffffff;
            padding: 8px 10px;
            border-radius: 999px;
            display: flex;
            align-items: center;
            gap: 4px;
            cursor: pointer;
            font-size: 13px;
        }

        .logout-btn:hover {
            background: rgba(255,255,255,0.25);
        }

        .content-wrapper {
            flex: 1;
            padding: 18px 16px 24px;
            background: linear-gradient(180deg, #f97316 0%, #f97316 18%, #f9fafb 18%, #f9fafb 100%);
        }

        .cards-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
            gap: 16px;
            max-width: 860px;
            margin: 0 auto 40px;
        }

        .menu-card {
            border-radius: 26px;
            padding: 24px 20px;
            color: #ffffff;
            text-decoration: none;
            display: flex;
            flex-direction: column;
            justify-content: center;
            gap: 14px;
            box-shadow: 0 12px 24px rgba(0,0,0,0.18);
            transition: transform 0.12s ease-out, box-shadow 0.12s ease-out;
        }

        .menu-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 16px 32px rgba(0,0,0,0.22);
        }

        .card-icon-wrapper {
            width: 56px;
            height: 56px;
            border-radius: 50%;
            background: rgba(255,255,255,0.16);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .card-icon {
            font-size: 30px;
        }

        .card-title {
            font-size: 20px;
            font-weight: 700;
        }

        .card-subtitle {
            font-size: 13px;
            opacity: 0.9;
        }

        .card-orange {
            background: #f97316;
        }

        .card-green {
            background: #16a34a;
        }

        .card-blue {
            background: #2563eb;
        }

        .card-purple {
            background: #7c3aed;
        }

        .logo-footer {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 6px;
            margin-top: 10px;
            color: #6b7280;
            font-size: 13px;
        }

        .logo-image {
            width: 120px;
            height: auto;
            object-fit: contain;
            opacity: 0.95;
        }

        @media (max-width: 640px) {
            .app-bar {
                padding-inline: 14px;
            }

            .cards-grid {
                grid-template-columns: 1fr 1fr;
            }
        }

        @media (max-width: 480px) {
            .cards-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>

<%
    ec.edu.monster.dto.UsuarioDTO usuarioSesion =
            (ec.edu.monster.dto.UsuarioDTO) session.getAttribute("usuarioSesion");
    String nombreUsuario = (usuarioSesion != null) ? usuarioSesion.getUsername() : "";
%>

<header class="app-bar">
    <div class="app-bar-left">
        <div class="avatar">
            <%= nombreUsuario.isEmpty() ? "EQ" :
                    nombreUsuario.substring(0, 1).toUpperCase() %>
        </div>
        <div class="title-block">
            <p class="title-main">ElectroQuito</p>
            <p class="title-sub">Bienvenido, <strong><%= nombreUsuario %></strong></p>
        </div>
    </div>
    <form method="get"
          action="${pageContext.request.contextPath}/electroquito/logout">
        <button type="submit" class="logout-btn">
            <span class="material-icons" style="font-size:18px;">logout</span>
            <span>Salir</span>
        </button>
    </form>
</header>

<main class="content-wrapper">
    <section class="cards-grid">

        <!-- Productos -->
        <a href="${pageContext.request.contextPath}/electroquito/productos"
           class="menu-card card-orange">
            <div class="card-icon-wrapper">
                <span class="material-icons card-icon">laptop</span>
            </div>
            <div>
                <div class="card-title">Productos</div>
                <div class="card-subtitle">
                    Consulta y administra el catálogo de electrodomésticos.
                </div>
            </div>
        </a>

        <!-- Nuevo Producto -->
        <a href="${pageContext.request.contextPath}/electroquito/productos/nuevo"
           class="menu-card card-green">
            <div class="card-icon-wrapper">
                <span class="material-icons card-icon">add</span>
            </div>
            <div>
                <div class="card-title">Nuevo Producto</div>
                <div class="card-subtitle">
                    Registra un nuevo producto con su código y precio de venta.
                </div>
            </div>
        </a>

        <!-- Facturar -->
        <!-- Facturar -->
        <button type="button"
                class="menu-card card-blue"
                onclick="window.location.href='${pageContext.request.contextPath}/electroquito/facturacion/nueva'">
            <div class="card-icon-wrapper">
                <span class="material-icons card-icon">shopping_cart</span>
            </div>
            <div>
                <div class="card-title">Facturar</div>
                <div class="card-subtitle">
                    Genera una nueva factura seleccionando cliente y producto.
                </div>
            </div>
        </button>


        <!-- Facturas -->
        <a href="${pageContext.request.contextPath}/electroquito/facturas"
           class="menu-card card-purple">
            <div class="card-icon-wrapper">
                <span class="material-icons card-icon">receipt_long</span>
            </div>
            <div>
                <div class="card-title">Facturas</div>
                <div class="card-subtitle">
                    Revisa el historial de facturas y sus detalles.
                </div>
            </div>
        </a>

    </section>

    <section class="logo-footer">
        <!-- Cambia la ruta de la imagen por la de tu proyecto -->
        <img src="${pageContext.request.contextPath}/resources/img/electroquito_logo.png"
             alt="ElectroQuito"
             class="logo-image">
        <span>Comercializadora de Electrodomésticos</span>
    </section>
</main>

</body>
</html>
