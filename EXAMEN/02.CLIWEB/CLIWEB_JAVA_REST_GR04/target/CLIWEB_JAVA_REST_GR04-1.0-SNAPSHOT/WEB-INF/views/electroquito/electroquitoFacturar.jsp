<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ElectroQuito - Nueva factura</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet">
    <style>
        :root {
            --eq-orange-primary: #f97316;
            --eq-orange-secondary: #ff8a3c;
            --eq-cream: #fff3e2;
            --eq-card: #fffdf8;
            --eq-border: #f7d9bf;
            --eq-text-dark: #1f2933;
            --eq-text-muted: #6b7280;
            --eq-green: #1e8a47;
            --eq-blue: #1d4ed8;
            --eq-navy: #0f172a;
        }

        *,
        *::before,
        *::after {
            box-sizing: border-box;
        }

        body.eq-invoice-body {
            margin: 0;
            font-family: 'Poppins', 'Segoe UI', Arial, sans-serif;
            color: var(--eq-text-dark);
            background: linear-gradient(180deg, #ff7a14 0%, #ff9330 38%, #fff4df 100%);
            min-height: 100vh;
        }

        a {
            color: inherit;
            text-decoration: none;
        }

        button {
            font-family: inherit;
        }

        .material-icons-round {
            font-size: 1.15rem;
            vertical-align: middle;
        }

        .eq-gradient-wrapper {
            padding: 0 24px 64px;
        }

        .eq-invoice-hero {
            max-width: 1200px;
            margin: 0 auto;
            padding: 32px 0 18px;
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            align-items: center;
            color: #fff;
        }

        .eq-hero-nav {
            display: flex;
            align-items: center;
            gap: 18px;
        }

        .eq-back-button {
            width: 48px;
            height: 48px;
            border-radius: 50%;
            border: none;
            background: rgba(255, 255, 255, 0.25);
            display: inline-flex;
            align-items: center;
            justify-content: center;
            color: #fff;
            cursor: pointer;
            transition: background 0.15s ease;
        }

        .eq-back-button:hover {
            background: rgba(255, 255, 255, 0.38);
        }

        .eq-hero-title {
            margin: 0;
            font-size: 2rem;
            font-weight: 700;
        }

        .eq-hero-subtitle {
            margin: 4px 0 0;
            font-weight: 500;
            opacity: 0.92;
        }

        .eq-hero-logout {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 10px 18px;
            border-radius: 999px;
            background: rgba(255, 255, 255, 0.18);
            color: #fff;
            font-weight: 500;
            transition: background 0.15s ease;
        }

        .eq-hero-logout:hover {
            background: rgba(255, 255, 255, 0.28);
        }

        .eq-invoice-main {
            max-width: 1200px;
            margin: 0 auto;
        }

        .eq-alert-stack {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .eq-alert {
            display: flex;
            gap: 10px;
            align-items: flex-start;
            border-radius: 20px;
            padding: 16px 18px;
            box-shadow: 0 18px 40px rgba(15, 23, 42, 0.12);
            font-size: 0.95rem;
            line-height: 1.4;
        }

        .eq-alert-error {
            background: #fef2f2;
            color: #b91c1c;
            border: 1px solid #fecaca;
        }

        .eq-alert-success {
            background: #f0fdf4;
            color: #15803d;
            border: 1px solid #bbf7d0;
        }

        .eq-invoice-form {
            margin-top: 26px;
            display: flex;
            flex-direction: column;
            gap: 26px;
        }

        .eq-form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
            gap: 22px;
        }

        .eq-form-card {
            background: linear-gradient(140deg, var(--eq-card), #fff1e6);
            border-radius: 32px;
            border: 1px solid var(--eq-border);
            box-shadow: 0 18px 40px rgba(249, 115, 22, 0.16);
            padding: 30px 34px;
        }

        .eq-card-span-2 {
            grid-column: span 2;
        }

        .eq-card-header {
            margin-bottom: 20px;
        }

        .eq-card-title {
            margin: 0;
            font-size: 1.25rem;
            font-weight: 700;
        }

        .eq-card-subtitle {
            margin: 6px 0 0;
            color: var(--eq-text-muted);
            font-size: 0.95rem;
        }

        .eq-field-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 18px;
        }

        .eq-field {
            display: block;
        }

        .eq-field-label {
            font-size: 0.85rem;
            font-weight: 600;
            color: #a5693f;
            text-transform: uppercase;
            letter-spacing: 0.04em;
        }

        .eq-input-wrapper {
            margin-top: 8px;
            display: flex;
            align-items: center;
            gap: 10px;
            background: #fff;
            border: 1px solid #f1d3bb;
            border-radius: 20px;
            padding: 12px 16px;
            box-shadow: inset 0 0 0 rgba(0,0,0,0);
        }

        .eq-input-wrapper input {
            border: none;
            outline: none;
            width: 100%;
            font-size: 1rem;
            color: var(--eq-text-dark);
            background: transparent;
        }

        .eq-input-wrapper input::placeholder {
            color: #c5c5c5;
        }

        .eq-select-product {
            width: 100%;
            border: none;
            border-radius: 999px;
            background: linear-gradient(90deg, var(--eq-orange-primary), var(--eq-orange-secondary));
            color: #fff;
            font-size: 1rem;
            font-weight: 600;
            padding: 14px 24px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            cursor: pointer;
            box-shadow: 0 18px 28px rgba(249, 115, 22, 0.35);
            margin-bottom: 18px;
            transition: transform 0.12s ease, box-shadow 0.12s ease, opacity 0.12s ease;
        }

        .eq-select-product:disabled {
            opacity: 0.5;
            cursor: not-allowed;
            box-shadow: none;
        }

        .eq-select-product:not(:disabled):hover {
            transform: translateY(-2px);
            box-shadow: 0 24px 32px rgba(249, 115, 22, 0.45);
        }

        .eq-selected-product {
            border: 1px dashed #f7c9a1;
            border-radius: 24px;
            padding: 18px 20px;
            background: #fff;
        }

        .eq-selected-placeholder {
            color: var(--eq-text-muted);
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .eq-selected-details {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 18px;
        }

        .eq-selected-thumb {
            width: 64px;
            height: 64px;
            border-radius: 20px;
            background: radial-gradient(circle at top, #fee2c5, #facba7);
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }

        .eq-selected-thumb img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .eq-selected-thumb span {
            font-size: 1.6rem;
            color: var(--eq-orange-primary);
        }

        .eq-selected-name {
            margin: 0;
            font-size: 1.05rem;
            font-weight: 600;
        }

        .eq-selected-code {
            margin: 4px 0 0;
            font-size: 0.9rem;
            color: var(--eq-text-muted);
        }

        .eq-selected-price {
            margin: 0;
            font-size: 1.35rem;
            font-weight: 700;
            color: var(--eq-green);
        }

        .eq-quantity-field {
            margin-top: 22px;
        }

        .eq-payment-pills {
            display: flex;
            flex-wrap: wrap;
            gap: 14px;
        }

        .eq-payment-pill {
            flex: 1 1 180px;
        }

        .eq-payment-pill input {
            display: none;
        }

        .eq-pill-body {
            border-radius: 999px;
            border: 1px solid rgba(0,0,0,0.08);
            padding: 12px 18px;
            display: flex;
            align-items: center;
            gap: 12px;
            font-weight: 600;
            color: var(--eq-text-dark);
            background: #fff;
            transition: background 0.15s ease, box-shadow 0.15s ease, color 0.15s ease;
        }

        .eq-payment-pill input:checked + .eq-pill-body {
            background: #182233;
            color: #fff;
            box-shadow: 0 12px 26px rgba(15, 23, 42, 0.25);
        }

        .eq-pill-caption {
            font-size: 0.8rem;
            font-weight: 500;
            opacity: 0.85;
        }

        .eq-form-actions {
            display: flex;
            justify-content: flex-end;
        }

        .eq-main-button {
            border: none;
            border-radius: 999px;
            padding: 18px 42px;
            font-size: 1rem;
            font-weight: 700;
            background: linear-gradient(90deg, #17853f, #1fad58);
            color: #fff;
            cursor: pointer;
            box-shadow: 0 22px 32px rgba(23, 133, 63, 0.35);
            display: inline-flex;
            align-items: center;
            gap: 10px;
            transition: transform 0.15s ease, box-shadow 0.15s ease;
        }

        .eq-main-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 28px 38px rgba(23, 133, 63, 0.45);
        }

        .eq-hint {
            margin-top: 8px;
            font-size: 0.85rem;
            color: var(--eq-text-muted);
        }

        .eq-empty-hint {
            margin-top: 12px;
            font-size: 0.92rem;
            color: #b45309;
            font-weight: 500;
        }

        .eq-modal {
            position: fixed;
            inset: 0;
            background: rgba(15, 23, 42, 0.45);
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
            opacity: 0;
            pointer-events: none;
            transition: opacity 0.2s ease;
            z-index: 50;
        }

        .eq-modal.is-active {
            opacity: 1;
            pointer-events: auto;
        }

        body.eq-modal-open {
            overflow: hidden;
        }

        .eq-modal-card {
            width: min(520px, 100%);
            background: #1f2937;
            color: #fff;
            border-radius: 30px;
            padding: 26px;
            box-shadow: 0 32px 65px rgba(0,0,0,0.45);
        }

        .eq-modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 18px;
        }

        .eq-modal-title {
            margin: 0;
            font-size: 1.25rem;
            font-weight: 600;
        }

        .eq-modal-close {
            border: none;
            background: rgba(255,255,255,0.12);
            color: #fff;
            border-radius: 999px;
            padding: 6px 12px;
            cursor: pointer;
        }

        .eq-product-list {
            display: flex;
            flex-direction: column;
            gap: 14px;
            max-height: 60vh;
            overflow-y: auto;
        }

        .eq-product-card {
            border: none;
            width: 100%;
            border-radius: 22px;
            padding: 18px 20px;
            background: linear-gradient(120deg, #fff5eb, #ffe3cc);
            color: var(--eq-text-dark);
            display: grid;
            grid-template-columns: auto 1fr auto;
            gap: 16px;
            align-items: center;
            cursor: pointer;
            text-align: left;
            transition: transform 0.12s ease, box-shadow 0.12s ease;
        }

        .eq-product-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 16px 32px rgba(255, 255, 255, 0.25);
        }

        .eq-product-thumb {
            width: 62px;
            height: 62px;
            border-radius: 20px;
            background: radial-gradient(circle at top, #fee2c5, #facba7);
            overflow: hidden;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .eq-product-thumb img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .eq-product-thumb span {
            font-size: 1.4rem;
            color: var(--eq-orange-primary);
        }

        .eq-product-icon {
            width: 48px;
            height: 48px;
            border-radius: 16px;
            background: rgba(249, 115, 22, 0.15);
            display: flex;
            align-items: center;
            justify-content: center;
            color: var(--eq-orange-primary);
            font-size: 1.6rem;
        }

        .eq-product-name {
            margin: 0;
            font-size: 1rem;
            font-weight: 600;
        }

        .eq-product-code {
            margin: 6px 0 0;
            color: var(--eq-text-muted);
            font-size: 0.9rem;
        }

        .eq-product-price {
            margin: 0;
            font-weight: 700;
            color: var(--eq-green);
            font-size: 1.1rem;
        }

        @media (max-width: 960px) {
            .eq-card-span-2 {
                grid-column: span 1;
            }
        }

        @media (max-width: 640px) {
            .eq-gradient-wrapper {
                padding: 0 16px 42px;
            }

            .eq-invoice-hero {
                flex-direction: column;
                gap: 14px;
                align-items: flex-start;
            }

            .eq-form-card {
                padding: 24px;
            }

            .eq-form-actions {
                justify-content: center;
            }

            .eq-selected-details {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body class="eq-invoice-body">

<c:set var="formaPagoSeleccionada"
       value="${empty param.formaPago ? 'EFECTIVO' : param.formaPago}"/>
<c:set var="cantidadSeleccionada"
       value="${empty param.cantidad ? 1 : param.cantidad}"/>
<c:set var="productoSeleccionadoId" value="${param.productoId}"/>
<c:set var="tieneProductos" value="${not empty productos}"/>

<div class="eq-gradient-wrapper">
    <header class="eq-invoice-hero">
        <div class="eq-hero-nav">
            <a class="eq-back-button"
               href="${pageContext.request.contextPath}/electroquito/home"
               title="Volver al men&uacute;">
                <span class="material-icons-round">arrow_back</span>
            </a>
            <div>
                <p class="eq-hero-title">Nueva factura</p>
                <p class="eq-hero-subtitle">
                    Completa los datos para registrar la venta
                </p>
            </div>
        </div>
        <a class="eq-hero-logout"
           href="${pageContext.request.contextPath}/electroquito/logout">
            <span class="material-icons-round">logout</span>
            <span>Salir</span>
        </a>
    </header>

    <main class="eq-invoice-main">
        <div class="eq-alert-stack">
            <c:if test="${not empty error}">
                <div class="eq-alert eq-alert-error">
                    <span class="material-icons-round">error</span>
                    <div><c:out value="${error}"/></div>
                </div>
            </c:if>
            <c:if test="${not empty exito}">
                <div class="eq-alert eq-alert-success">
                    <span class="material-icons-round">check_circle</span>
                    <div><c:out value="${exito}"/></div>
                </div>
            </c:if>
        </div>

        <form class="eq-invoice-form"
              id="facturaForm"
              method="post"
              action="${pageContext.request.contextPath}/electroquito/facturacion/nueva"
              data-has-success="${not empty exito}">

            <div class="eq-form-grid">
                <section class="eq-form-card eq-card-span-2">
                    <div class="eq-card-header">
                        <p class="eq-card-title">Datos del cliente</p>
                        <p class="eq-card-subtitle">
                            Identifica al comprador antes de generar la factura.
                        </p>
                    </div>

                    <div class="eq-field-grid">
                        <label class="eq-field">
                            <span class="eq-field-label">C&eacute;dula</span>
                            <div class="eq-input-wrapper">
                                <span class="material-icons-round">badge</span>
                                <input type="text"
                                       name="cedula"
                                       maxlength="13"
                                       placeholder="0000000000"
                                       value="${fn:escapeXml(param.cedula)}"
                                       required>
                            </div>
                        </label>

                        <label class="eq-field">
                            <span class="eq-field-label">Nombre completo</span>
                            <div class="eq-input-wrapper">
                                <span class="material-icons-round">person</span>
                                <input type="text"
                                       name="nombreCompleto"
                                       placeholder="Cliente ElectroQuito"
                                       value="${fn:escapeXml(param.nombreCompleto)}"
                                       required>
                            </div>
                        </label>
                    </div>
                </section>

                <section class="eq-form-card">
                    <div class="eq-card-header">
                        <p class="eq-card-title">Producto</p>
                        <p class="eq-card-subtitle">
                            Selecciona un electrodom&eacute;stico disponible.
                        </p>
                    </div>

                    <button type="button"
                            id="selectProductButton"
                            class="eq-select-product"
                            <c:if test="${not tieneProductos}">disabled</c:if>>
                        <span class="material-icons-round">point_of_sale</span>
                        <span>Seleccionar producto</span>
                    </button>

                    <div class="eq-selected-product">
                        <div class="eq-selected-placeholder"
                             id="selectedProductEmpty">
                            <span class="material-icons-round">inventory_2</span>
                            <span>Selecciona un producto del cat&aacute;logo</span>
                        </div>
                        <div class="eq-selected-details" id="selectedProductDetails" hidden>
                            <div class="eq-selected-thumb" id="selectedProductThumb">
                                <span class="material-icons-round" id="selectedProductThumbIcon">inventory_2</span>
                                <img id="selectedProductThumbImg" src="" alt="Producto seleccionado" hidden>
                            </div>
                            <div>
                                <p class="eq-selected-name"
                                   id="selectedProductName">Producto</p>
                                <p class="eq-selected-code"
                                   id="selectedProductCode">C&oacute;digo</p>
                            </div>
                            <p class="eq-selected-price"
                               id="selectedProductPrice">$0.00</p>
                        </div>
                    </div>

                    <input type="hidden"
                           name="productoId"
                           id="productoIdInput"
                           value="${fn:escapeXml(productoSeleccionadoId)}">

                    <div class="eq-quantity-field">
                        <label class="eq-field">
                            <span class="eq-field-label">Cantidad</span>
                            <div class="eq-input-wrapper">
                                <span class="material-icons-round">shopping_cart</span>
                                <input type="number"
                                       id="cantidadInput"
                                       min="1"
                                       step="1"
                                       name="cantidad"
                                       value="${fn:escapeXml(cantidadSeleccionada)}"
                                       required>
                            </div>
                        </label>
                        <p class="eq-hint">
                            Cada producto aplica stock inmediato. Cantidad m&iacute;nima: 1.
                        </p>
                    </div>

                    <c:if test="${not tieneProductos}">
                        <p class="eq-empty-hint">
                            No existen productos disponibles. Registra uno nuevo antes de facturar.
                        </p>
                    </c:if>
                </section>

                <section class="eq-form-card">
                    <div class="eq-card-header">
                        <p class="eq-card-title">Forma de pago</p>
                        <p class="eq-card-subtitle">
                            Aplica descuentos especiales seg&uacute;n el tipo de pago.
                        </p>
                    </div>

                    <div class="eq-payment-pills">
                        <label class="eq-payment-pill">
                            <input type="radio"
                                   name="formaPago"
                                   value="EFECTIVO"
                                   <c:if test="${formaPagoSeleccionada eq 'EFECTIVO'}">checked</c:if>>
                            <span class="eq-pill-body">
                                <span class="material-icons-round">attach_money</span>
                                <span>
                                    Efectivo
                                    <div class="eq-pill-caption">(33% desc.)</div>
                                </span>
                            </span>
                        </label>

                        <label class="eq-payment-pill">
                            <input type="radio"
                                   name="formaPago"
                                   value="CREDITO"
                                   <c:if test="${formaPagoSeleccionada eq 'CREDITO'}">checked</c:if>>
                            <span class="eq-pill-body">
                                <span class="material-icons-round">credit_score</span>
                                <span>
                                    Cr&eacute;dito
                                    <div class="eq-pill-caption">Pago diferido</div>
                                </span>
                            </span>
                        </label>
                    </div>
                </section>
            </div>

            <div class="eq-form-actions">
                <button type="submit" class="eq-main-button">
                    <span class="material-icons-round">receipt_long</span>
                    <span>Generar factura</span>
                </button>
            </div>
        </form>
    </main>
</div>

<div class="eq-modal" id="productModal" data-has-products="${tieneProductos}">
    <div class="eq-modal-card" role="dialog" aria-modal="true">
        <div class="eq-modal-header">
            <p class="eq-modal-title">Seleccionar producto</p>
            <button type="button" class="eq-modal-close" id="closeProductModal">
                Cerrar
            </button>
        </div>
        <div class="eq-product-list">
            <c:forEach var="producto" items="${productos}">
                <c:choose>
                    <c:when test="${empty producto.imagenUrl}">
                        <c:set var="imagenSrc" value=""/>
                    </c:when>
                    <c:when test="${fn:startsWith(producto.imagenUrl, 'http')}">
                        <c:set var="imagenSrc" value="${producto.imagenUrl}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="imagenSrc" value="${imagenBaseUrl.concat(producto.imagenUrl)}"/>
                    </c:otherwise>
                </c:choose>
                <button type="button"
                        class="eq-product-card"
                        data-product-id="${producto.id}"
                        data-product-name="${fn:escapeXml(producto.nombre)}"
                        data-product-code="${fn:escapeXml(producto.codigo)}"
                        data-product-price="${producto.precioVenta}"
                        data-product-image="${imagenSrc}">
                    <div class="eq-product-thumb">
                        <c:choose>
                            <c:when test="${not empty imagenSrc}">
                                <img src="${imagenSrc}" alt="${producto.nombre}">
                            </c:when>
                            <c:otherwise>
                                <span class="material-icons-round">kitchen</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div>
                        <p class="eq-product-name">
                            <c:out value="${producto.nombre}"/>
                        </p>
                        <p class="eq-product-code">
                            C&oacute;digo: <c:out value="${producto.codigo}"/>
                        </p>
                    </div>
                    <p class="eq-product-price">
                        <fmt:formatNumber value="${producto.precioVenta}"
                                          type="currency"
                                          currencySymbol="$"
                                          minFractionDigits="2"
                                          maxFractionDigits="2"/>
                    </p>
                </button>
            </c:forEach>

            <c:if test="${empty productos}">
                <p>No hay productos cargados.</p>
            </c:if>
        </div>
    </div>
</div>

<script>
    (function () {
        const modal = document.getElementById('productModal');
        const selectButton = document.getElementById('selectProductButton');
        const closeButton = document.getElementById('closeProductModal');
        const productCards = modal.querySelectorAll('.eq-product-card');
        const productIdInput = document.getElementById('productoIdInput');
        const placeholder = document.getElementById('selectedProductEmpty');
        const details = document.getElementById('selectedProductDetails');
        const thumbContainer = document.getElementById('selectedProductThumb');
        const thumbIcon = document.getElementById('selectedProductThumbIcon');
        const thumbImg = document.getElementById('selectedProductThumbImg');
        const nameEl = document.getElementById('selectedProductName');
        const codeEl = document.getElementById('selectedProductCode');
        const priceEl = document.getElementById('selectedProductPrice');
        const quantityInput = document.getElementById('cantidadInput');
        const form = document.getElementById('facturaForm');

        const formatPrice = (value) => {
            const number = Number(value);
            if (Number.isNaN(number)) {
                return '$0.00';
            }
            return new Intl.NumberFormat('es-EC', {
                style: 'currency',
                currency: 'USD',
                minimumFractionDigits: 2
            }).format(number);
        };

        const updateSelectedProduct = (card) => {
            if (!card) {
                productIdInput.value = '';
                details.hidden = true;
                placeholder.hidden = false;
                if (thumbImg) {
                    thumbImg.hidden = true;
                }
                if (thumbIcon) {
                    thumbIcon.style.display = 'inline-flex';
                }
                return;
            }
            productIdInput.value = card.dataset.productId || '';
            const image = card.dataset.productImage || '';

            if (image) {
                thumbImg.src = image;
                thumbImg.hidden = false;
                thumbIcon.style.display = 'none';
            } else {
                thumbImg.hidden = true;
                thumbIcon.style.display = 'inline-flex';
            }

            nameEl.textContent = card.dataset.productName || 'Producto ElectroQuito';
            codeEl.textContent = card.dataset.productCode
                ? 'Codigo: ' + card.dataset.productCode
                : 'Codigo no disponible';
            priceEl.textContent = formatPrice(card.dataset.productPrice);
            details.hidden = false;
            placeholder.hidden = true;
        };

        const openModal = () => {
            if (!selectButton || selectButton.disabled) {
                return;
            }
            modal.classList.add('is-active');
            document.body.classList.add('eq-modal-open');
        };

        const closeModal = () => {
            modal.classList.remove('is-active');
            document.body.classList.remove('eq-modal-open');
        };

        if (selectButton) {
            selectButton.addEventListener('click', openModal);
        }

        if (closeButton) {
            closeButton.addEventListener('click', closeModal);
        }

        modal.addEventListener('click', (event) => {
            if (event.target === modal) {
                closeModal();
            }
        });

        document.addEventListener('keydown', (event) => {
            if (event.key === 'Escape' && modal.classList.contains('is-active')) {
                closeModal();
            }
        });

        productCards.forEach((card) => {
            card.addEventListener('click', () => {
                updateSelectedProduct(card);
                closeModal();
            });
        });

        const presetProductId = productIdInput.value;
        if (presetProductId) {
            const match = Array.from(productCards)
                .find((card) => card.dataset.productId === presetProductId);
            if (match) {
                updateSelectedProduct(match);
            }
        }

        if (form && form.dataset.hasSuccess === 'true') {
            form.reset();
            Array.from(form.querySelectorAll('input[type="text"]')).forEach((input) => {
                input.value = '';
            });
            if (quantityInput) {
                quantityInput.value = 1;
            }
            const efectivo = form.querySelector('input[name="formaPago"][value="EFECTIVO"]');
            if (efectivo) {
                efectivo.checked = true;
            }
            updateSelectedProduct(null);
        }
    })();
</script>
</body>
</html>
