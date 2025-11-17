<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>ElectroQuito – Productos</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Fuente similar a la del móvil -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap"
          rel="stylesheet">

    <style>
        :root {
            --eq-orange: #f97316;
            --eq-orange-dark: #ea580c;
            --eq-green: #16a34a;
            --eq-surface: #fff7ed;
            --eq-bg: #f9fafb;
            --eq-text-main: #111827;
            --eq-text-muted: #9ca3af;
            --eq-card-radius: 28px;
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: "Poppins", system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background: linear-gradient(to bottom, var(--eq-orange) 0%, #ffedd5 40%, var(--eq-bg) 100%);
            min-height: 100vh;
            color: var(--eq-text-main);
        }

        .eq-app-shell {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        /* Barra superior – similar a la app */
        .eq-topbar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 14px 32px;
            color: #ffffff;
        }

        .eq-topbar-left {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .eq-back-btn {
            width: 34px;
            height: 34px;
            border-radius: 999px;
            border: 1px solid rgba(255, 255, 255, 0.3);
            display: inline-flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
            color: #ffffff;
            font-size: 18px;
            backdrop-filter: blur(4px);
        }

        .eq-app-title {
            font-size: 22px;
            font-weight: 700;
        }

        .eq-topbar-right {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .eq-user-chip {
            font-size: 13px;
            padding: 4px 10px;
            border-radius: 999px;
            background: rgba(255, 255, 255, 0.18);
            border: 1px solid rgba(255, 255, 255, 0.32);
        }

        .eq-logout-btn {
            width: 34px;
            height: 34px;
            border-radius: 12px;
            background: #ffffff;
            color: var(--eq-orange-dark);
            display: inline-flex;
            justify-content: center;
            align-items: center;
            text-decoration: none;
            font-size: 18px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.18);
        }

        /* Contenido principal */
        .eq-main {
            flex: 1;
            display: flex;
            justify-content: center;
            padding: 12px 16px 64px;
        }

        .eq-main-inner {
            width: 100%;
            max-width: 540px;
        }

        .eq-surface-card {
            background: rgba(255, 255, 255, 0.98);
            border-radius: var(--eq-card-radius);
            box-shadow: 0 22px 45px rgba(15, 23, 42, 0.22);
            padding: 22px 20px 26px;
        }

        .eq-section-title {
            font-size: 24px;
            font-weight: 700;
            margin: 0 0 4px;
        }

        .eq-section-subtitle {
            margin: 0 0 18px;
            font-size: 13px;
            color: #6b7280;
        }

        /* Buscador */
        .eq-search-row {
            display: flex;
            gap: 10px;
            margin-bottom: 16px;
        }

        .eq-search-input {
            flex: 1;
            border-radius: 999px;
            border: 1px solid #e5e7eb;
            padding: 10px 14px;
            font-size: 13px;
            outline: none;
        }

        .eq-search-input:focus {
            border-color: var(--eq-orange);
            box-shadow: 0 0 0 1px rgba(249, 115, 22, 0.3);
        }

        .eq-search-btn {
            border-radius: 999px;
            border: none;
            background: var(--eq-orange-dark);
            color: #ffffff;
            padding: 0 18px;
            font-size: 13px;
            font-weight: 600;
            cursor: pointer;
            white-space: nowrap;
        }

        .eq-error-message {
            margin: 0 0 12px;
            font-size: 13px;
            padding: 10px 12px;
            border-radius: 14px;
            background: #fee2e2;
            color: #b91c1c;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .eq-error-icon {
            font-size: 16px;
        }

        .eq-empty {
            margin: 8px 0 0;
            font-size: 13px;
            color: #6b7280;
        }

        /* Lista de productos */
        .eq-products-list {
            margin-top: 10px;
            display: flex;
            flex-direction: column;
            gap: 14px;
        }

        .eq-product-card {
            display: flex;
            align-items: center;
            padding: 14px 16px;
            border-radius: 22px;
            background: linear-gradient(135deg, #fff7ed, #ffffff);
            box-shadow: 0 14px 30px rgba(15, 23, 42, 0.10);
        }

        .eq-product-icon {
            flex: 0 0 56px;
            height: 56px;
            border-radius: 999px;
            margin-right: 14px;
            background: radial-gradient(circle at 30% 20%, #fed7aa, #ffedd5);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .eq-product-icon span {
            font-size: 26px;
            color: var(--eq-orange-dark);
        }

        .eq-product-info {
            flex: 1;
            min-width: 0;
        }

        .eq-product-name {
            margin: 0;
            font-size: 16px;
            font-weight: 600;
            color: var(--eq-text-main);
        }

        .eq-product-code {
            margin: 4px 0 0;
            font-size: 13px;
            color: var(--eq-text-muted);
        }

        .eq-product-price {
            margin-left: 10px;
            font-size: 18px;
            font-weight: 700;
            color: var(--eq-green);
            white-space: nowrap;
        }

        /* FAB – botón flotante "+" similar al móvil */
        .eq-fab {
            position: fixed;
            right: 26px;
            bottom: 26px;
            width: 64px;
            height: 64px;
            border-radius: 999px;
            background: var(--eq-orange);
            color: #ffffff;
            font-size: 32px;
            display: flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
            box-shadow: 0 18px 40px rgba(248, 113, 22, 0.75);
            border: none;
        }

        .eq-fab:hover {
            transform: translateY(-1px);
            box-shadow: 0 22px 50px rgba(248, 113, 22, 0.9);
        }

        /* Responsive */
        @media (max-width: 640px) {
            .eq-topbar {
                padding-inline: 18px;
            }
            .eq-main {
                padding-inline: 10px;
            }
        }

    </style>
</head>
<body>
<div class="eq-app-shell">

    <!-- TOP BAR -->
    <header class="eq-topbar">
        <div class="eq-topbar-left">
            <a class="eq-back-btn"
               href="${pageContext.request.contextPath}/electroquito/home"
               title="Atrás">
                ←
            </a>
            <div class="eq-app-title">ElectroQuito</div>
        </div>

        <div class="eq-topbar-right">
            <div class="eq-user-chip">
                <c:choose>
                    <c:when test="${not empty usuarioSesion}">
                        Bienvenido,
                        <strong>
                            <c:out value="${usuarioSesion.username}"/>
                        </strong>
                    </c:when>
                    <c:otherwise>
                        Sesión
                    </c:otherwise>
                </c:choose>
            </div>
            <a class="eq-logout-btn"
               href="${pageContext.request.contextPath}/electroquito/logout"
               title="Salir">
                ⇨
            </a>
        </div>
    </header>

    <!-- CONTENIDO -->
    <main class="eq-main">
        <div class="eq-main-inner">
            <section class="eq-surface-card">

                <h1 class="eq-section-title">Productos</h1>
                <p class="eq-section-subtitle">
                    Consulta y administra el catálogo de electrodomésticos.
                </p>

                <!-- Buscador -->
                <form method="get" class="eq-search-row">
                    <input type="text"
                           name="q"
                           class="eq-search-input"
                           placeholder="Buscar por nombre o código"
                           value="${fn:escapeXml(filtro)}">
                    <button type="submit" class="eq-search-btn">Buscar</button>
                </form>

                <!-- Mensaje de error si algo falló -->
                <c:if test="${not empty error}">
                    <div class="eq-error-message">
                        <span class="eq-error-icon">⚠</span>
                        <span><c:out value="${error}"/></span>
                    </div>
                </c:if>

                <!-- Lista de productos -->
                <c:choose>
                    <c:when test="${empty productos}">
                        <p class="eq-empty">
                            No hay productos registrados.
                        </p>
                    </c:when>
                    <c:otherwise>
                        <div class="eq-products-list">
                            <fmt:setLocale value="es_EC"/>
                            <c:forEach var="p" items="${productos}">
                                <article class="eq-product-card">
                                    <div class="eq-product-icon">
                                        <span>🖥</span>
                                    </div>
                                    <div class="eq-product-info">
                                        <h2 class="eq-product-name">
                                            <c:out value="${p.nombre}"/>
                                        </h2>
                                        <div class="eq-product-code">
                                            Código:
                                            <c:out value="${p.codigo}"/>
                                        </div>
                                    </div>
                                    <div class="eq-product-price">
                                        $
                                        <fmt:formatNumber value="${p.precioVenta}"
                                                          type="number"
                                                          minFractionDigits="2"
                                                          maxFractionDigits="2"/>
                                    </div>
                                </article>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>

            </section>
        </div>
    </main>

    <!-- FAB para ir a "Nuevo Producto" -->
    <a href="${pageContext.request.contextPath}/electroquito/productos/nuevo"
       class="eq-fab"
       title="Nuevo producto">
        +
    </a>

</div>
</body>
</html>
