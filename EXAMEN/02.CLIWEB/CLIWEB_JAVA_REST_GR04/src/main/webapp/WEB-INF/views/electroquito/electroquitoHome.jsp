<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>ElectroQuito - Inicio</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet">

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(180deg, #f97316 0%, #f97316 24%, #f9fafb 24%, #f9fafb 100%);
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .eq-hero {
            width: min(1200px, 100%);
            padding: 28px 32px 12px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: #fff;
            gap: 18px;
        }

        .eq-hero-left {
            display: flex;
            align-items: center;
            gap: 18px;
        }

        .eq-hero-avatar {
            width: 72px;
            height: 72px;
            border-radius: 24px;
            background: #fed0a3;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
            box-shadow: 0 12px 25px rgba(0, 0, 0, 0.25);
        }

        .eq-hero-avatar img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .eq-hero-text {
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .eq-hero-brand {
            margin: 0;
            font-size: 1.85rem;
            font-weight: 700;
        }

        .eq-hero-welcome {
            margin: 6px 0 0;
            font-size: 1rem;
            letter-spacing: 0.01em;
        }

        .eq-logout-btn {
            border: none;
            border-radius: 16px;
            background: rgba(255,255,255,0.17);
            color: #fff;
            padding: 10px 18px;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.15s ease;
        }

        .eq-logout-btn:hover {
            background: rgba(255,255,255,0.28);
        }

        .eq-main {
            width: min(1100px, 100%);
            margin-top: 32px;
            padding: 32px 32px 60px;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 36px;
        }

        .eq-card-grid {
            width: 100%;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(180px, 210px));
            justify-content: center;
            gap: 24px;
        }

        .eq-menu-card {
            height: 210px;
            border-radius: 34px;
            color: #fff;
            text-decoration: none;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 16px;
            font-size: 1.1rem;
            font-weight: 600;
            box-shadow: 0 18px 34px rgba(15,23,42,0.18);
            transition: transform 0.12s ease, box-shadow 0.12s ease;
        }

        .eq-menu-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 26px 40px rgba(15,23,42,0.25);
        }

        .eq-card-icon {
            font-size: 46px;
        }

        .eq-card-orange { background: #f97316; }
        .eq-card-green  { background: #16a34a; }
        .eq-card-blue   { background: #2563eb; }
        .eq-card-purple { background: #9333ea; }

        .eq-logo-section {
            text-align: center;
            color: #475569;
            line-height: 1.4;
        }

        .eq-logo-section img {
            width: 140px;
            height: auto;
            object-fit: contain;
            margin-bottom: 8px;
        }

        @media (max-width: 768px) {
            .eq-hero {
                flex-direction: column;
                align-items: flex-start;
                padding-inline: 20px;
            }

            .eq-main {
                padding-inline: 20px;
            }
        }

        @media (max-width: 520px) {
            .eq-card-grid {
                grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
            }

            .eq-hero-avatar {
                width: 60px;
                height: 60px;
                font-size: 26px;
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

<div class="eq-hero">
    <div class="eq-hero-left">
        <div class="eq-hero-avatar">
            <img src="<c:url value='/assets/img/monster.jpg'/>" alt="Usuario">
        </div>
        <div class="eq-hero-text">
            <p class="eq-hero-brand">ElectroQuito</p>
            <p class="eq-hero-welcome">Bienvenido, <strong><%= nombreUsuario %></strong></p>
        </div>
    </div>
    <form method="get"
          action="${pageContext.request.contextPath}/electroquito/logout">
        <button type="submit" class="eq-logout-btn">
            <span class="material-icons-round">logout</span>
            <span>Salir</span>
        </button>
    </form>
</div>

<main class="eq-main">
    <section class="eq-card-grid">
        <a href="${pageContext.request.contextPath}/electroquito/productos"
           class="eq-menu-card eq-card-orange">
            <span class="material-icons-round eq-card-icon">laptop</span>
            <span>Productos</span>
        </a>

        <a href="${pageContext.request.contextPath}/electroquito/productos/nuevo"
           class="eq-menu-card eq-card-green">
            <span class="material-icons-round eq-card-icon">add</span>
            <span>Nuevo Producto</span>
        </a>

        <a href="${pageContext.request.contextPath}/electroquito/facturacion/nueva"
           class="eq-menu-card eq-card-blue">
            <span class="material-icons-round eq-card-icon">shopping_cart</span>
            <span>Facturar</span>
        </a>

        <a href="${pageContext.request.contextPath}/electroquito/facturas"
           class="eq-menu-card eq-card-purple">
            <span class="material-icons-round eq-card-icon">receipt_long</span>
            <span>Facturas</span>
        </a>
    </section>

    <section class="eq-logo-section">
        <img src="<c:url value='/assets/img/electroquito.jpg'/>"
             alt="ElectroQuito">
        <p>Comercializadora de Electrodom&eacute;sticos</p>
    </section>
</main>

</body>
</html>
