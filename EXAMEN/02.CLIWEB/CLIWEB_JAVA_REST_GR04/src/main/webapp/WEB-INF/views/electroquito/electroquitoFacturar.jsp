<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>ElectroQuito - Inicio</title>
    <style>
        body {
            margin: 0;
            font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background: #f5f5f5;
        }

        .eq-appbar {
            background: #f46a08;
            color: #fff;
            padding: 18px 40px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .eq-appbar-left {
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .eq-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: #ffd7aa;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 700;
            font-size: 18px;
            color: #f46a08;
        }

        .eq-title-block {
            display: flex;
            flex-direction: column;
        }

        .eq-title {
            font-size: 24px;
            font-weight: 700;
        }

        .eq-subtitle {
            font-size: 14px;
            opacity: 0.85;
        }

        .eq-logout-btn {
            border-radius: 999px;
            border: none;
            background: rgba(255,255,255,0.18);
            color: #fff;
            padding: 8px 16px;
            font-size: 13px;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 6px;
        }

        .eq-main {
            padding: 40px 40px 80px;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 40px;
        }

        .eq-card-grid {
            display: grid;
            grid-template-columns: repeat(4, minmax(180px, 220px));
            gap: 28px;
            max-width: 1000px;
            width: 100%;
        }

        .eq-main-card {
            border-radius: 28px;
            padding: 24px 22px;
            color: #fff;
            text-decoration: none;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            min-height: 180px;
            box-shadow: 0 12px 24px rgba(0,0,0,0.18);
            transition: transform 0.12s ease, box-shadow 0.12s ease;
        }

        .eq-main-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 18px 30px rgba(0,0,0,0.25);
        }

        .eq-main-card-orange { background: #ff7a14; }
        .eq-main-card-green  { background: #16a34a; }
        .eq-main-card-blue   { background: #2563eb; }
        .eq-main-card-purple { background: #7c3aed; }

        .eq-main-card-icon {
            font-size: 38px;
            margin-bottom: 18px;
        }

        .eq-main-card-title {
            font-size: 20px;
            font-weight: 700;
            margin-bottom: 8px;
        }

        .eq-main-card-text {
            font-size: 14px;
            line-height: 1.4;
            opacity: 0.9;
        }

        .eq-footer-logo {
            margin-top: 40px;
            text-align: center;
            color: #f46a08;
            font-weight: 600;
            font-size: 18px;
        }

        @media (max-width: 1024px) {
            .eq-card-grid {
                grid-template-columns: repeat(2, minmax(180px, 1fr));
            }
        }

        @media (max-width: 640px) {
            .eq-card-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>

<header class="eq-appbar">
    <div class="eq-appbar-left">
        <div class="eq-avatar">M</div>
        <div class="eq-title-block">
            <div class="eq-title">ElectroQuito</div>
            <div class="eq-subtitle">
                Bienvenido,
                <c:out value="${sessionScope.usuarioSesionElectroquito.username}" default="MONSTER"/>
            </div>
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/electroquito/logout" method="post">
        <button class="eq-logout-btn" type="submit">
            ⏏ Salir
        </button>
    </form>
</header>

<main class="eq-main">

    <section class="eq-card-grid">

        <!-- PRODUCTOS -->
        <a class="eq-main-card eq-main-card-orange"
           href="${pageContext.request.contextPath}/electroquito/productos">
            <div class="eq-main-card-icon">💻</div>
            <div class="eq-main-card-title">Productos</div>
            <div class="eq-main-card-text">
                Consulta y administra el catálogo de electrodomésticos.
            </div>
        </a>

        <!-- NUEVO PRODUCTO -->
        <a class="eq-main-card eq-main-card-green"
           href="${pageContext.request.contextPath}/electroquito/productos/nuevo">
            <div class="eq-main-card-icon">➕</div>
            <div class="eq-main-card-title">Nuevo Producto</div>
            <div class="eq-main-card-text">
                Registra un nuevo producto con código y precio de venta.
            </div>
        </a>

        <!-- FACTURAR -->
        <a class="eq-main-card eq-main-card-blue"
           href="${pageContext.request.contextPath}/electroquito/facturacion/nueva">
            <div class="eq-main-card-icon">🛒</div>
            <div class="eq-main-card-title">Facturar</div>
            <div class="eq-main-card-text">
                Genera una nueva factura seleccionando cliente y producto.
            </div>
        </a>

        <!-- FACTURAS -->
        <a class="eq-main-card eq-main-card-purple"
           href="${pageContext.request.contextPath}/electroquito/facturas">
            <div class="eq-main-card-icon">🧾</div>
            <div class="eq-main-card-title">Facturas</div>
            <div class="eq-main-card-text">
                Revisa el historial de facturas y sus detalles.
            </div>
        </a>

    </section>

    <div class="eq-footer-logo">
        ElectroQuito – Comercializadora de Electrodomésticos
    </div>

</main>

</body>
</html>
