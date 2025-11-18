<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>ElectroQuito â€“ Productos</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Fuente similar a la del mÃ³vil -->
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

        .eq-main {
            flex: 1;
            display: flex;
            justify-content: center;
            padding: 32px 36px 80px;
        }

        .eq-main-inner {
            width: 100%;
            max-width: 1100px;
        }

        .eq-surface-card {
            background: rgba(255, 255, 255, 0.96);
            border-radius: var(--eq-card-radius);
            box-shadow: 0 28px 60px rgba(15, 23, 42, 0.25);
            padding: 36px 42px;
        }

        .eq-section-title {
            font-size: clamp(1.9rem, 3vw, 2.4rem);
            font-weight: 700;
            margin: 0 0 6px;
        }

        .eq-section-subtitle {
            margin: 0 0 22px;
            font-size: 1rem;
            color: #6b7280;
        }

        .eq-products-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 12px;
            margin-bottom: 14px;
        }

        .eq-meta-count {
            font-weight: 600;
            color: var(--eq-orange-dark);
        }

        .eq-search-row {
            display: flex;
            gap: 12px;
            margin-bottom: 16px;
        }

        .eq-search-input {
            flex: 1;
            border-radius: 18px;
            border: 1px solid #e5e7eb;
            padding: 12px 16px;
            font-size: 0.95rem;
            outline: none;
        }

        .eq-search-input:focus {
            border-color: var(--eq-orange);
            box-shadow: 0 0 0 1px rgba(249, 115, 22, 0.3);
        }

        .eq-search-btn {
            border-radius: 14px;
            border: none;
            background: var(--eq-orange-dark);
            color: #ffffff;
            padding: 0 22px;
            font-size: 0.95rem;
            font-weight: 600;
            cursor: pointer;
            white-space: nowrap;
            box-shadow: 0 12px 20px rgba(234, 88, 12, 0.25);
        }

        .eq-error-message {
            margin: 0 0 12px;
            font-size: 0.9rem;
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
            font-size: 0.95rem;
            color: #6b7280;
        }

        .eq-products-list {
            margin-top: 18px;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
            gap: 20px;
        }

        .eq-product-card {
            display: flex;
            align-items: center;
            gap: 18px;
            padding: 18px 20px;
            border-radius: 26px;
            background: linear-gradient(130deg, #ffffff, #fff6eb 65%, rgba(255,255,255,0.95));
            box-shadow: 0 22px 40px rgba(15, 23, 42, 0.18);
        }

        .eq-product-thumb {
            flex: 0 0 72px;
            height: 72px;
            border-radius: 24px;
            background: radial-gradient(circle at 30% 20%, #fed7aa, #ffedd5);
            overflow: hidden;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .eq-product-thumb img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            display: block;
        }

        .eq-thumb-placeholder {
            font-size: 26px;
            color: var(--eq-orange-dark);
        }

        .eq-product-info h2 {
            margin: 0;
            font-size: 1.05rem;
        }

        .eq-product-code {
            font-size: 0.9rem;
            color: #6b7280;
            margin-top: 2px;
        }

        .eq-product-price {
            margin-left: auto;
            font-weight: 700;
            color: var(--eq-green);
            font-size: 1.15rem;
        }

        .eq-fab {
            position: fixed;
            right: 26px;
            bottom: 24px;
            width: 56px;
            height: 56px;
            border-radius: 999px;
            background: linear-gradient(135deg, var(--eq-orange), var(--eq-orange-dark));
            color: #ffffff;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
            font-size: 26px;
            box-shadow: 0 20px 35px rgba(249, 115, 22, 0.35);
        }

        @media (max-width: 900px) {
            .eq-main {
                padding: 22px 18px 64px;
            }
            .eq-surface-card {
                padding: 26px;
            }
            .eq-products-list {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 640px) {
            .eq-topbar {
                padding-inline: 18px;
            }
            .eq-main {
                padding: 20px 14px 60px;
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
               title="AtrÃ¡s">
                â†
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
                        SesiÃ³n
                    </c:otherwise>
                </c:choose>
            </div>
            <a class="eq-logout-btn"
               href="${pageContext.request.contextPath}/electroquito/logout"
               title="Salir">
                â‡¨
            </a>
        </div>
    </header>

    <!-- CONTENIDO -->
    <main class="eq-main">
        <div class="eq-main-inner">
            <section class="eq-surface-card">

                <h1 class="eq-section-title">Productos</h1>
                <p class="eq-section-subtitle">
                    Consulta y administra el catÃ¡logo de electrodomÃ©sticos.
                </p>

                <c:set var="totalProductos" value="${fn:length(productos)}"/>
                <div class="eq-products-meta">
                    <span class="eq-meta-label">Catálogo general</span>
                    <span class="eq-meta-count">
                        <strong><c:out value="${totalProductos}"/></strong> disponibles
                    </span>
                </div>

                <!-- Buscador -->
                <form method="get" class="eq-search-row">
                    <input type="text"
                           name="q"
                           class="eq-search-input"
                           placeholder="Buscar por nombre o cÃ³digo"
                           value="${fn:escapeXml(filtro)}">
                    <button type="submit" class="eq-search-btn">Buscar</button>
                </form>

                <!-- Mensaje de error si algo fallÃ³ -->
                <c:if test="${not empty error}">
                    <div class="eq-error-message">
                        <span class="eq-error-icon">âš </span>
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
                                <c:choose>
                                    <c:when test="${empty p.imagenUrl}">
                                        <c:set var="imagenSrc" value=""/>
                                    </c:when>
                                    <c:when test="${fn:startsWith(p.imagenUrl, 'http')}">
                                        <c:set var="imagenSrc" value="${p.imagenUrl}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="imagenSrc" value="${imagenBaseUrl.concat(p.imagenUrl)}"/>
                                    </c:otherwise>
                                </c:choose>
                                <article class="eq-product-card">
                                    <div class="eq-product-thumb">
                                        <c:choose>
                                            <c:when test="${not empty imagenSrc}">
                                                <img src="${imagenSrc}" alt="${p.nombre}">
                                            </c:when>
                                            <c:otherwise>
                                                <span class="eq-thumb-placeholder">&#128722;</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="eq-product-info">
                                        <h2 class="eq-product-name">
                                            <c:out value="${p.nombre}"/>
                                        </h2>
                                        <div class="eq-product-code">
                                            CÃ³digo:
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


