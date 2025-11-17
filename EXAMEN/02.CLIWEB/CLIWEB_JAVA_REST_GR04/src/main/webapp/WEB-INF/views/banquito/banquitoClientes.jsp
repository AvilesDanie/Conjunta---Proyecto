<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.dto.ClienteDTO"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Clientes</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
</head>
<body class="bg-gradient">
<div class="app-container">

    <!-- Header superior tipo app -->
    <header class="page-header">
        <div class="page-header-left">
            <a href="<%=request.getContextPath()%>/banquito/home" class="back-link">
                &larr;
            </a>
            <div>
                <div class="page-title">Lista de Clientes</div>
                <div class="page-subtitle">Buscar por nombre o cédula</div>
            </div>
        </div>
    </header>

    <main class="page-main">

        <!-- Barra de búsqueda -->
        <form class="search-bar"
              method="get"
              action="<%=request.getContextPath()%>/banquito/clientes">
            <span class="search-icon">&#128269;</span>
            <input type="text"
                   name="q"
                   placeholder="Buscar por nombre o cédula"
                   value="<%= request.getAttribute("filtro") != null ? request.getAttribute("filtro") : "" %>">
            <button type="submit" class="btn-chip">Buscar</button>
        </form>

        <!-- Mensaje de error si falló el REST -->
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <div class="alert-error"><%= error %></div>
        <% } %>

        <%
            List<ClienteDTO> clientes = (List<ClienteDTO>) request.getAttribute("clientes");
            if (clientes == null || clientes.isEmpty()) {
        %>
            <p class="empty-text">No hay clientes registrados.</p>
        <%
            } else {
        %>

        <!-- Lista de tarjetas de clientes -->
        <section class="card-list">

            <%
                for (ClienteDTO cli : clientes) {
                    String inicial = cli.getInicial();
            %>
            <a class="client-card"
               href="<%=request.getContextPath()%>/banquito/clientes/detalle?id=<%=cli.id%>">

                <div class="client-avatar">
                    <span><%= inicial %></span>
                </div>

                <div class="client-info">
                    <div class="client-name"><%= cli.nombre %></div>
                    <div class="client-meta">
                        CI: <span><%= cli.cedula %></span>
                    </div>

                    <% if (cli.cuentaPrincipal != null && !cli.cuentaPrincipal.isBlank()) { %>
                    <div class="client-meta">
                        Cuenta: <span><%= cli.cuentaPrincipal %></span>
                        &nbsp;&bull;&nbsp;
                        Tipo: <span><%= cli.tipoCuentaPrincipal %></span>
                    </div>
                    <% } %>
                </div>

                <div class="client-arrow">
                    &#8250;
                </div>
            </a>
            <% } %>

        </section>
        <% } %>

    </main>
</div>
</body>
</html>
