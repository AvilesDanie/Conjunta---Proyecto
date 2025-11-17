<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Evaluar Crédito - BanQuito</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-gradient">
<div class="app-container credit-page">

    <!-- Flecha de regreso -->
    <a href="${pageContext.request.contextPath}/banquito/creditos" class="back-link">
        <span>&larr;</span> Atrás
    </a>

    <!-- Título principal -->
    <header class="page-header">
        <h1 class="page-title">Evaluar Crédito</h1>
    </header>

    <!-- Banner de información (tarjeta celeste de arriba) -->
    <section class="info-banner">
        <div class="info-banner__icon">i</div>
        <div class="info-banner__content">
            <h2 class="info-banner__title">Evaluación de Crédito</h2>
            <p class="info-banner__text">
                Complete los datos para evaluar si el cliente es sujeto de crédito
                según su historial de movimientos y su capacidad de pago.
            </p>
        </div>
    </section>

    <!-- Mensajes -->
    <%
        String error = (String) request.getAttribute("error");
        String exito = (String) request.getAttribute("exito");
        String cedulaVal = (String) request.getAttribute("cedula");
        String precioVal = (String) request.getAttribute("precioProducto");
        String plazoVal  = (String) request.getAttribute("plazoMeses");
        String cuentaVal = (String) request.getAttribute("numeroCuenta");
    %>

    <% if (error != null) { %>
        <div class="alert-error"><%= error %></div>
    <% } %>

    <% if (exito != null) { %>
        <div class="alert-success"><%= exito %></div>
    <% } %>

    <!-- Tarjeta oscura con el formulario -->
    <section class="form-card form-card-dark">
        <form action="${pageContext.request.contextPath}/banquito/creditos/evaluar" method="post" class="form-vertical">

            <!-- Cédula -->
            <div class="form-group form-group-lined">
                <label for="cedula" class="form-label">Cédula del Cliente *</label>
                <div class="input-wrapper">
                    <input
                        type="text"
                        id="cedula"
                        name="cedula"
                        class="form-control form-control-dark"
                        placeholder="Ingrese la cédula"
                        value="<%= cedulaVal != null ? cedulaVal : "" %>"
                        required
                    >
                </div>
            </div>

            <!-- Precio del producto -->
            <div class="form-group form-group-lined">
                <label for="precioProducto" class="form-label">Precio del Producto *</label>
                <div class="input-wrapper">
                    <input
                        type="number"
                        step="0.01"
                        min="0"
                        id="precioProducto"
                        name="precioProducto"
                        class="form-control form-control-dark"
                        placeholder="Ej: 1500.00"
                        value="<%= precioVal != null ? precioVal : "" %>"
                        required
                    >
                </div>
            </div>

            <!-- Plazo en meses -->
            <div class="form-group form-group-lined">
                <label for="plazoMeses" class="form-label">Plazo en Meses *</label>
                <div class="input-wrapper">
                    <input
                        type="number"
                        min="1"
                        id="plazoMeses"
                        name="plazoMeses"
                        class="form-control form-control-dark"
                        placeholder="Ej: 12"
                        value="<%= plazoVal != null ? plazoVal : "" %>"
                        required
                    >
                </div>
            </div>

            <!-- Número de cuenta para crédito -->
            <div class="form-group form-group-lined">
                <label for="numeroCuenta" class="form-label">Número de Cuenta para Crédito *</label>
                <div class="input-wrapper">
                    <input
                        type="text"
                        id="numeroCuenta"
                        name="numeroCuenta"
                        class="form-control form-control-dark"
                        placeholder="Ej: CTA-0002-2024"
                        value="<%= cuentaVal != null ? cuentaVal : "" %>"
                        required
                    >
                </div>
            </div>

            <!-- Botón principal -->
            <div class="form-actions">
                <button type="submit" class="btn-primary btn-primary-lg btn-full">
                    <span class="btn-primary-label">Evaluar Crédito</span>
                </button>
            </div>
        </form>
    </section>

</div>
</body>
</html>
