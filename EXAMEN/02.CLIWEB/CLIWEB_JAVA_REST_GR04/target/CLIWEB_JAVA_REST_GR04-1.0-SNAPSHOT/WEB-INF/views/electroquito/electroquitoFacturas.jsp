<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<fmt:setLocale value="es_EC" scope="page"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ElectroQuito - Historial de facturas</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet">
    <style>
        :root {
            --eq-orange-strong: #f97316;
            --eq-orange-soft: #ffb570;
            --eq-card-bg: #fffdf8;
            --eq-card-border: #f7d9bf;
            --eq-text-dark: #0f172a;
            --eq-text-muted: #6b7280;
            --eq-green: #15803d;
            --eq-blue: #2563eb;
        }

        *,
        *::before,
        *::after {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: 'Poppins', 'Segoe UI', Arial, sans-serif;
            background: linear-gradient(180deg, #ff7a14 0%, #ff9330 26%, #fff4df 100%);
            color: var(--eq-text-dark);
            min-height: 100vh;
        }

        .eq-wrapper {
            padding: 0 24px 64px;
            max-width: 1200px;
            margin: 0 auto;
        }

        .eq-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 32px 0 12px;
            color: #fff;
            flex-wrap: wrap;
            gap: 18px;
        }

        .eq-header-left {
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .eq-back-btn {
            width: 48px;
            height: 48px;
            border-radius: 50%;
            border: none;
            background: rgba(255, 255, 255, 0.25);
            color: #fff;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: background 0.15s ease;
        }

        .eq-back-btn:hover {
            background: rgba(255, 255, 255, 0.32);
        }

        .eq-header-title {
            margin: 0;
            font-size: 2rem;
            font-weight: 700;
        }

        .eq-header-subtitle {
            margin: 4px 0 0;
            font-size: 0.95rem;
            opacity: 0.85;
        }

        .eq-header-actions {
            display: inline-flex;
            gap: 10px;
        }

        .eq-header-actions a {
            border-radius: 999px;
            padding: 10px 18px;
            background: rgba(255, 255, 255, 0.18);
            color: #fff;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            text-decoration: none;
            font-weight: 500;
        }

        .eq-header-actions a:hover {
            background: rgba(255, 255, 255, 0.28);
        }

        .eq-panel {
            background: linear-gradient(135deg, var(--eq-card-bg), #fff1e4);
            border-radius: 32px;
            border: 1px solid var(--eq-card-border);
            box-shadow: 0 20px 45px rgba(249, 115, 22, 0.15);
            padding: 26px 28px 32px;
            margin-top: 26px;
        }

        .eq-search-form {
            display: flex;
            flex-wrap: wrap;
            gap: 14px;
            align-items: center;
            margin-bottom: 16px;
        }

        .eq-search-field {
            flex: 1 1 280px;
            display: flex;
            align-items: center;
            background: #fff;
            border-radius: 999px;
            padding: 10px 16px;
            gap: 10px;
            border: 1px solid #f1d3bb;
        }

        .eq-search-field input {
            border: none;
            outline: none;
            font-size: 1rem;
            width: 100%;
            background: transparent;
        }

        .eq-btn {
            border: none;
            border-radius: 999px;
            padding: 12px 24px;
            font-weight: 600;
            font-size: 0.95rem;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            cursor: pointer;
        }

        .eq-btn-primary {
            background: linear-gradient(90deg, #f97316, #fb923c);
            color: #fff;
            box-shadow: 0 8px 18px rgba(249, 115, 22, 0.35);
        }

        .eq-btn-secondary {
            background: rgba(15, 23, 42, 0.08);
            color: var(--eq-text-dark);
        }

        .eq-alert {
            border-radius: 20px;
            padding: 14px 16px;
            display: flex;
            gap: 10px;
            align-items: flex-start;
            margin-bottom: 18px;
        }

        .eq-alert-error {
            background: #fef2f2;
            color: #b91c1c;
            border: 1px solid #fecaca;
        }

        .eq-list {
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        .eq-bill-card {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 18px;
            padding: 22px 24px;
            border-radius: 28px;
            border: 1px solid var(--eq-card-border);
            background: linear-gradient(120deg, #fffdfa, #fff2e1);
            box-shadow: 0 12px 24px rgba(15, 23, 42, 0.08);
        }

        .eq-bill-info h3 {
            margin: 0 0 8px;
            font-size: 1.1rem;
        }

        .eq-bill-info p {
            margin: 2px 0;
            color: var(--eq-text-muted);
            font-size: 0.92rem;
        }

        .eq-bill-total {
            text-align: right;
            min-width: 180px;
            display: flex;
            justify-content: flex-end;
            gap: 12px;
            align-items: center;
        }

        .eq-pill-icon {
            width: 56px;
            height: 56px;
            border-radius: 50%;
            background: var(--eq-blue);
            display: flex;
            align-items: center;
            justify-content: center;
            color: #fff;
        }

        .eq-total-label {
            margin: 0;
            font-weight: 600;
            color: var(--eq-green);
        }

        .eq-empty-state {
            text-align: center;
            padding: 40px 10px;
            color: var(--eq-text-muted);
            font-size: 1rem;
        }

        .eq-fab {
            position: fixed;
            right: 28px;
            bottom: 28px;
            width: 58px;
            height: 58px;
            border-radius: 50%;
            border: none;
            background: #ff7a14;
            color: #fff;
            box-shadow: 0 15px 30px rgba(249, 115, 22, 0.4);
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 1.6rem;
        }

        @media (max-width: 640px) {
            .eq-wrapper {
                padding: 0 16px 42px;
            }

            .eq-bill-card {
                flex-direction: column;
                align-items: flex-start;
            }

            .eq-bill-total {
                justify-content: space-between;
                width: 100%;
            }
        }
    </style>
</head>
<body>

<c:set var="facturas" value="${facturas}" />
<c:set var="filtroActual" value="${filtro}" />

<div class="eq-wrapper">
    <header class="eq-header">
        <div class="eq-header-left">
            <a class="eq-back-btn" href="${pageContext.request.contextPath}/electroquito/home">
                <span class="material-icons-round">arrow_back</span>
            </a>
            <div>
                <p class="eq-header-title">Historial de facturas</p>
                <p class="eq-header-subtitle">
                    Consulta las ventas registradas recientemente.
                </p>
            </div>
        </div>
        <div class="eq-header-actions">
            <a href="${pageContext.request.contextPath}/electroquito/facturacion/nueva">
                <span class="material-icons-round">add</span>
                <span>Nueva factura</span>
            </a>
            <a href="${pageContext.request.contextPath}/electroquito/logout">
                <span class="material-icons-round">logout</span>
                <span>Salir</span>
            </a>
        </div>
    </header>

    <section class="eq-panel">
        <form class="eq-search-form" method="get"
              action="${pageContext.request.contextPath}/electroquito/facturas">
            <label class="eq-search-field" for="facturaSearch">
                <span class="material-icons-round" style="color:#f97316;">search</span>
                <input type="text"
                       id="facturaSearch"
                       name="q"
                       placeholder="Buscar por n&uacute;mero, nombre o c&eacute;dula"
                       value="${fn:escapeXml(filtroActual)}">
            </label>
            <button type="submit" class="eq-btn eq-btn-primary">
                <span class="material-icons-round">manage_search</span>
                <span>Buscar</span>
            </button>
            <c:if test="${not empty filtroActual}">
                <a class="eq-btn eq-btn-secondary"
                   href="${pageContext.request.contextPath}/electroquito/facturas">
                    <span class="material-icons-round">close</span>
                    <span>Limpiar</span>
                </a>
            </c:if>
        </form>

        <c:if test="${not empty error}">
            <div class="eq-alert eq-alert-error">
                <span class="material-icons-round">error</span>
                <span><c:out value="${error}"/></span>
            </div>
        </c:if>

        <div class="eq-list">
            <c:if test="${empty facturas}">
                <div class="eq-empty-state">
                    <p>No hay facturas para mostrar.</p>
                    <p>Registra una venta o ajusta el filtro de b&uacute;squeda.</p>
                </div>
            </c:if>

            <c:forEach var="factura" items="${facturas}">
                <article class="eq-bill-card">
                    <div class="eq-bill-info">
                        <h3>Factura #<c:out value="${factura.id}"/></h3>
                        <p>
                            Cliente:
                            <strong><c:out value="${factura.clienteNombre}"/></strong>
                        </p>
                        <p>C&eacute;dula: <c:out value="${factura.clienteCedula}"/></p>
                        <p>Fecha: <c:out value="${factura.fechaEmision}"/></p>
                    </div>
                    <div class="eq-bill-total">
                        <div class="eq-pill-icon">
                            <span class="material-icons-round">receipt_long</span>
                        </div>
                        <p class="eq-total-label">
                            Total:
                            <fmt:formatNumber value="${factura.total}"
                                              type="currency"
                                              currencySymbol="$"
                                              minFractionDigits="2"
                                              maxFractionDigits="2"/>
                        </p>
                    </div>
                </article>
            </c:forEach>
        </div>
    </section>
</div>

<button type="button" class="eq-fab" aria-label="Ir a buscar"
        onclick="document.getElementById('facturaSearch').focus();">
    <span class="material-icons-round">search</span>
</button>

</body>
</html>
