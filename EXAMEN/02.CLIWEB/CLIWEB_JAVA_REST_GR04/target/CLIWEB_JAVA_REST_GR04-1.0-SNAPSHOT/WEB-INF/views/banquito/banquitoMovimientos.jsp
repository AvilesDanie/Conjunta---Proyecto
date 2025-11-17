<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.dto.MovimientoDTO"%>

<%
    @SuppressWarnings("unchecked")
    List<MovimientoDTO> movimientos =
        (List<MovimientoDTO>) request.getAttribute("movimientos");
    String numeroCuenta = (String) request.getAttribute("numeroCuenta");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Movimientos</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
</head>
<body class="bg-gradient">
<div class="app-container">

    <!-- HEADER estilo BanQuito -->
    <header class="app-header">
        <a href="<%=request.getContextPath()%>/banquito/cuentas" class="back-link">
            <span>&larr;</span>
        </a>
        <div class="header-titles">
            <div class="header-title">Movimientos</div>
            <% if (numeroCuenta != null) { %>
                <div class="header-subtitle">Cuenta <%= numeroCuenta %></div>
            <% } %>
        </div>
    </header>

    <section class="page-card page-card--list">
        <% if (error != null) { %>
            <div class="alert-error"><%= error %></div>
        <% } %>

        <% if (movimientos != null && !movimientos.isEmpty()) { %>

            <% for (MovimientoDTO m : movimientos) {
                   String cssTipo;
                   if ("DEPOSITO".equalsIgnoreCase(m.tipo)) {
                       cssTipo = "mov-card--ingreso";
                   } else if ("RETIRO".equalsIgnoreCase(m.tipo)) {
                       cssTipo = "mov-card--egreso";
                   } else {
                       cssTipo = "mov-card--transfer";
                   }
            %>
                <article class="mov-card <%= cssTipo %>">
                    <div class="mov-main-row">
                        <div class="mov-title"><%= m.tipo %></div>
                        <div class="mov-amount">
                            <%= ("DEPOSITO".equalsIgnoreCase(m.tipo) ? "+" : "-") %>
                            $<%= String.format("%,.2f", m.monto) %>
                        </div>
                    </div>
                    <div class="mov-date">
                        <%= m.fecha %>
                    </div>
                </article>
            <% } %>

        <% } else if (error == null) { %>
            <p class="empty-state">
                No hay movimientos registrados para esta cuenta.
            </p>
        <% } %>
    </section>
</div>
</body>
</html>
