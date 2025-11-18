<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="ec.edu.monster.dto.ClienteDTO"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Clientes</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet">
    <style>
        body.bq-customers {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', 'Segoe UI', Arial, sans-serif;
            background: linear-gradient(180deg, #0f62d8 0%, #1d4ed8 28%, #111b5d 28%, #0b1035 100%);
            display: block;
            padding-bottom: 70px;
        }

        .bqc-wrapper {
            width: min(960px, 100%);
            margin: 0 auto;
            padding: 28px 18px 0;
            display: flex;
            flex-direction: column;
            gap: 24px;
        }

        .bqc-header {
            color: #fff;
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .bqc-back {
            width: 48px;
            height: 48px;
            border-radius: 16px;
            background: rgba(255,255,255,0.18);
            display: inline-flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
            color: #fff;
        }

        .bqc-title {
            margin: 0;
            font-size: 2rem;
            font-weight: 700;
        }

        .bqc-subtitle {
            margin: 4px 0 0;
            font-size: 1rem;
            opacity: 0.9;
        }

        .bqc-search-card {
            background: linear-gradient(180deg, #162475, #131a4e);
            padding: 24px;
            border-radius: 28px;
            box-shadow: 0 30px 60px rgba(7, 11, 43, 0.5);
        }

        .bqc-search-form {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .bqc-search-input {
            flex: 1 1 260px;
            display: flex;
            align-items: center;
            background: #fff;
            border-radius: 18px;
            padding: 0 14px;
        }

        .bqc-search-input span {
            color: #1d4ed8;
            margin-right: 6px;
        }

        .bqc-search-input input {
            border: none;
            background: transparent;
            padding: 12px 10px;
            width: 100%;
            outline: none;
            font-size: 1rem;
            font-family: inherit;
        }

        .bqc-search-button {
            border: none;
            border-radius: 999px;
            background: linear-gradient(120deg, #22d3ee, #2563eb);
            color: #fff;
            padding: 12px 26px;
            font-weight: 600;
            font-size: 1rem;
            cursor: pointer;
            box-shadow: 0 16px 30px rgba(34, 211, 238, 0.25);
        }

        .bqc-list {
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        .bqc-card {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 16px;
            border-radius: 30px;
            padding: 18px 20px;
            text-decoration: none;
            background: linear-gradient(120deg, #ffffff, #eff4ff);
            box-shadow: 0 20px 45px rgba(4, 8, 28, 0.25);
        }

        .bqc-card-info {
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .bqc-avatar {
            width: 52px;
            height: 52px;
            border-radius: 50%;
            background: #1d4ed8;
            color: #fff;
            font-weight: 600;
            font-size: 1.2rem;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .bqc-details h3 {
            margin: 0;
            font-size: 1.05rem;
            color: #0f172a;
        }

        .bqc-details p {
            margin: 2px 0;
            color: #475569;
            font-size: 0.9rem;
        }

        .bqc-card .material-icons-round {
            color: #2563eb;
            font-size: 1.4rem;
        }

        .bqc-empty,
        .alert-error {
            border-radius: 18px;
        }

        @media (max-width: 640px) {
            .bqc-card {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body class="bq-customers">
<div class="bqc-wrapper">

    <header class="bqc-header">
        <a class="bqc-back" href="<%=request.getContextPath()%>/banquito/home">
            <span class="material-icons-round">arrow_back</span>
        </a>
        <div>
            <p class="bqc-title">Lista de Clientes</p>
            <p class="bqc-subtitle">Buscar por nombre o cédula</p>
        </div>
    </header>

    <%
        String filtro = (String) request.getAttribute("filtro");
        String filtroValue = filtro != null ? filtro : "";
    %>

    <section class="bqc-search-card">
        <form class="bqc-search-form"
              method="get"
              action="<%=request.getContextPath()%>/banquito/clientes">
            <div class="bqc-search-input">
                <span class="material-icons-round">search</span>
                <input type="text"
                       name="q"
                       placeholder="Buscar por nombre o cédula"
                       value="<%= filtroValue %>">
            </div>
            <button type="submit" class="bqc-search-button">Buscar</button>
        </form>
    </section>

    <%
        String error = (String) request.getAttribute("error");
        List<ClienteDTO> clientes = (List<ClienteDTO>) request.getAttribute("clientes");
    %>

    <% if (error != null) { %>
    <div class="alert-error"><%= error %></div>
    <% } else if (clientes == null || clientes.isEmpty()) { %>
    <p class="bqc-empty" style="color:#e0e7ff;text-align:center;">
        No hay clientes registrados.
    </p>
    <% } else { %>
    <section class="bqc-list">
        <% for (ClienteDTO cli : clientes) { %>
        <%
            String cedulaHref = cli.cedula != null ? cli.cedula : "";
            String nombreHref = cli.nombre != null ? cli.nombre : "";
            String urlDetalle = String.format("%s/banquito/cuentas/cliente?cedula=%s&nombre=%s",
                    request.getContextPath(),
                    URLEncoder.encode(cedulaHref, "UTF-8"),
                    URLEncoder.encode(nombreHref, "UTF-8"));
        %>
        <a class="bqc-card"
           href="<%= urlDetalle %>">
            <div class="bqc-card-info">
                <div class="bqc-avatar">
                    <%= cli.getInicial() %>
                </div>
                <div class="bqc-details">
                    <h3><%= cli.nombre %></h3>
                    <p>CI: <%= cli.cedula %></p>
                    <% if (cli.cuentaPrincipal != null && !cli.cuentaPrincipal.isBlank()) { %>
                    <p>Cuenta: <%= cli.cuentaPrincipal %> | Tipo: <%= cli.tipoCuentaPrincipal %></p>
                    <% } %>
                </div>
            </div>
            <span class="material-icons-round">chevron_right</span>
        </a>
        <% } %>
    </section>
    <% } %>

</div>
</body>
</html>
