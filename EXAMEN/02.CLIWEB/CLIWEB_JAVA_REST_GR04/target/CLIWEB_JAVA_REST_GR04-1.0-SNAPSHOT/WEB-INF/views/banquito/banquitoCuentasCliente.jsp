<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="ec.edu.monster.dto.CuentaDTO"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Cuentas del Cliente</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
</head>
<body class="bg-gradient">
<div class="app-container">

    <header class="page-header">
        <div class="page-header-left">
            <a href="<%=request.getContextPath()%>/banquito/clientes" class="back-link">
                &larr;
            </a>
            <div>
                <div class="page-title">Cuentas del Cliente</div>
                <div class="page-subtitle">
                    <%= request.getAttribute("nombre") != null ? request.getAttribute("nombre") : "" %>
                </div>
            </div>
        </div>
    </header>

    <main class="page-main">

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <div class="alert-error"><%= error %></div>
        <% } %>

        <%
            List<CuentaDTO> cuentas = (List<CuentaDTO>) request.getAttribute("cuentas");
            NumberFormat nf = (NumberFormat) request.getAttribute("monedaFormat");

            if (cuentas == null || cuentas.isEmpty()) {
        %>
        <p class="empty-text">El cliente aún no tiene cuentas registradas.</p>
        <% } else { %>

        <section class="account-list">
            <%
                for (CuentaDTO cta : cuentas) {
                    String saldoStr = nf != null ? nf.format(cta.saldo).replace(" ", "") : String.valueOf(cta.saldo);
            %>
            <div class="account-card">
                <div class="account-header">
                    <div class="account-number"><%= cta.numero %></div>
                    <div class="account-type">Tipo: <%= cta.tipo %></div>
                </div>

                <div class="account-client">
                    <%= cta.nombreCliente != null ? cta.nombreCliente : "" %>
                </div>

                <div class="account-divider"></div>

                <div class="account-balance-row">
                    <span class="account-balance-label">Saldo</span>
                    <span class="account-balance"><%= saldoStr %></span>
                </div>

                <a class="account-mov-link"
                   href="<%=request.getContextPath()%>/banquito/cuentas/movimientos?numero=<%=java.net.URLEncoder.encode(cta.numero, "UTF-8")%>">
                    Ver movimientos
                </a>
            </div>
            <% } %>
        </section>

        <% } %>

        <!-- Botón flotante "+" (por ahora solo estético) -->
        <button type="button" class="fab-button" disabled>+</button>

    </main>
</div>
</body>
</html>
