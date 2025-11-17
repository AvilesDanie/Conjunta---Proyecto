<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.dto.CuentaDTO"%>

<%
    @SuppressWarnings("unchecked")
    List<CuentaDTO> cuentas = (List<CuentaDTO>) request.getAttribute("cuentas");
    String cedulaBuscada = (String) request.getAttribute("cedulaBuscada");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Cuentas</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
</head>
<body class="bg-gradient">
<div class="app-container">

    <!-- HEADER, pegado al estilo que ya usas -->
    <header class="app-header">
        <a href="<%=request.getContextPath()%>/banquito/home" class="back-link">
            <span>&larr;</span>
        </a>
        <div class="header-titles">
            <div class="header-title">Cuentas del Cliente</div>
            <div class="header-subtitle">Consultar cuentas por cédula</div>
        </div>
    </header>

    <!-- TARJETA DE BÚSQUEDA -->
    <section class="page-card page-card--top">
        <div class="section-title">Buscar cuentas</div>
        <p class="section-subtitle">
            Ingrese la cédula del cliente para listar sus cuentas.
        </p>

        <form class="form-inline" method="get" action="cuentas">
            <div class="form-group-inline">
                <input class="form-control"
                       type="text"
                       name="cedula"
                       placeholder="Cédula del cliente"
                       value="<%= cedulaBuscada != null ? cedulaBuscada : "" %>">
                <button type="submit" class="btn-primary">Buscar</button>
            </div>
        </form>

        <% if (error != null) { %>
            <div class="alert-error"><%= error %></div>
        <% } %>
    </section>

    <!-- LISTA DE CUENTAS (cards) -->
    <section class="page-card page-card--list">
        <% if (cuentas != null && !cuentas.isEmpty()) { %>

            <% for (CuentaDTO c : cuentas) { %>
                <article class="account-card">
                    <div class="account-header">
                        <div class="account-number">
                            Cuenta N° <%= c.numero %>
                        </div>
                        <div class="account-type">
                            Tipo: <span><%= c.tipo %></span>
                        </div>
                    </div>

                    <div class="account-body">
                        <div class="account-client-name">
                            <%= c.clienteNombre %>
                        </div>
                        <div class="account-client-id">
                            CI: <%= c.clienteCedula %>
                        </div>
                    </div>

                    <div class="account-footer">
                        <div class="account-balance-label">Saldo</div>
                        <div class="account-balance">
                            $<%= String.format("%,.2f", c.saldo) %>
                        </div>
                    </div>

                    <div class="account-actions">
                        <a href="<%=request.getContextPath()%>/banquito/movimientos?cuenta=<%= c.numero %>"
                           class="btn-secondary btn-full">
                            Ver movimientos →
                        </a>
                    </div>
                </article>
            <% } %>

        <% } else if (cedulaBuscada != null && !cedulaBuscada.isBlank()) { %>
            <p class="empty-state">
                No se encontraron cuentas para la cédula indicada.
            </p>
        <% } else { %>
            <p class="empty-state">
                Busque una cédula para ver las cuentas.
            </p>
        <% } %>
    </section>
</div>
</body>
</html>
