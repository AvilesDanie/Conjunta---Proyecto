<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tabla de Amortización - BanQuito</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-gradient">
<div class="app-container cuotas-page">

    <!-- Flecha de regreso -->
    <a href="${pageContext.request.contextPath}/banquito/creditos" class="back-link">
        <span>&larr;</span> Atrás
    </a>

    <!-- Cabecera -->
    <header class="page-header">
        <h1 class="page-title">Tabla de Amortización</h1>
    </header>

    <%
        String error = (String) request.getAttribute("error");
    %>

    <% if (error != null) { %>
        <div class="alert-error"><%= error %></div>
    <% } %>

    <!-- Tarjeta de búsqueda -->
    <section class="form-card amortizacion-search-card">
        <h2 class="amortizacion-subtitle">Consultar cuotas de un crédito</h2>
        <p class="amortizacion-text">
            Ingrese el ID de un crédito y pulse <strong>Consultar</strong> para ver la tabla de amortización.
        </p>

        <form action="${pageContext.request.contextPath}/banquito/cuotas" method="get" class="form-vertical">
            <div class="form-group form-group-lined">
                <label for="idCredito" class="form-label">ID del crédito</label>
                <div class="input-wrapper">
                    <input
                        type="number"
                        min="1"
                        id="idCredito"
                        name="idCredito"
                        class="form-control form-control-light"
                        placeholder="Ej: 10"
                        required
                    >
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-primary btn-primary-lg btn-full">
                    Consultar
                </button>
            </div>
        </form>
    </section>

    <!-- Zona de resultados (cuando luego mostremos la tabla/lista de cuotas) -->
    <!--
    <section class="cuotas-result-section">
        <article class="cuota-summary-card">
            <h2 class="cuota-summary-title">Crédito #10</h2>
            <p class="cuota-summary-line">
                <span>Total de cuotas:</span>
                <strong>3</strong>
            </p>
            <p class="cuota-summary-line">
                <span>Cuota mensual:</span>
                <strong>$444,49</strong>
            </p>
        </article>

        <div class="cuotas-list">
            <article class="cuota-card">
                <div class="cuota-card-header">
                    <h3 class="cuota-card-title">Cuota #1</h3>
                    <span class="status-pill status-pill-pendiente">Pendiente</span>
                </div>
                <div class="cuota-card-body">
                    <p><span>Valor Cuota:</span> <strong>$444,49</strong></p>
                    <p><span>Interés:</span> <strong>$120,00</strong></p>
                    <p><span>Capital:</span> <strong>$324,49</strong></p>
                    <p><span>Saldo Pendiente:</span> <strong class="saldo-pendiente">$8.675,51</strong></p>
                </div>
            </article>

            <!-- Repetir <article class="cuota-card"> por cada cuota -->
        </div>
    </section>
    -->

</div>
</body>
</html>
