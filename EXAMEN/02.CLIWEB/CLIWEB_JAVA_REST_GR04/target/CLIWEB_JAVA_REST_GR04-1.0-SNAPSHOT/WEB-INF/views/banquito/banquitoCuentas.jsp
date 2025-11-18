<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.dto.CuentaDTO"%>

<%
    @SuppressWarnings("unchecked")
    List<CuentaDTO> cuentas = (List<CuentaDTO>) request.getAttribute("cuentas");
    String cedulaBuscada = (String) request.getAttribute("cedulaBuscada");
    String error = (String) request.getAttribute("error");
    String nuevaCuentaCedula = (String) request.getAttribute("nuevaCuentaCedula");
    String nuevaCuentaTipo = (String) request.getAttribute("nuevaCuentaTipo");
    String nuevaCuentaSaldo = (String) request.getAttribute("nuevaCuentaSaldo");
    String nuevaCuentaError = (String) request.getAttribute("nuevaCuentaError");
    String nuevaCuentaExito = (String) request.getAttribute("nuevaCuentaExito");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Cuentas</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet">
    <style>
        body.bg-gradient.bq-accounts {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', 'Segoe UI', Arial, sans-serif;
            background: linear-gradient(180deg, #0f5ccf 0%, #1d4ed8 35%, #111b5f 35%, #e3f2ff 100%);
            display: block;
            padding-bottom: 56px;
        }

        .bq-accounts-wrapper {
            width: min(960px, 100%);
            margin: 0 auto;
            padding: 28px 18px 0;
            display: flex;
            flex-direction: column;
            gap: 22px;
        }

        .bq-accounts-header {
            display: flex;
            align-items: center;
            gap: 16px;
            color: #fff;
        }

        .bq-accounts-back {
            width: 48px;
            height: 48px;
            border-radius: 16px;
            background: rgba(255,255,255,0.2);
            color: #fff;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
        }

        .bq-accounts-header-text h1 {
            margin: 0;
            font-size: 2rem;
            font-weight: 700;
        }

        .bq-accounts-header-text p {
            margin: 4px 0 0;
            font-size: 1rem;
        }

        .bq-search-card,
        .bq-new-account-card {
            border-radius: 28px;
            padding: 24px;
            background: linear-gradient(185deg, #162475, #0f1b5a);
            box-shadow: 0 25px 48px rgba(11, 16, 52, 0.5);
        }

        .bq-search-field {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
        }

        .bq-input {
            flex: 1 1 240px;
            display: flex;
            align-items: center;
            border-radius: 18px;
            background: #fff;
            padding: 0 12px;
        }

        .bq-input input {
            border: none;
            outline: none;
            width: 100%;
            padding: 12px 8px;
            font-size: 1rem;
            font-family: inherit;
        }

        .bq-search-button {
            border: none;
            border-radius: 999px;
            padding: 14px 26px;
            font-weight: 600;
            font-size: 1rem;
            color: #fff;
            background: linear-gradient(90deg, #2563eb, #4f46e5);
            cursor: pointer;
            box-shadow: 0 18px 32px rgba(37,99,235,0.35);
        }

        .bq-new-account-card {
            background: linear-gradient(185deg, #11144a, #0b0f2f);
        }

        .bq-new-account-card h2 {
            margin: 0 0 8px;
            color: #dbe0ff;
            font-size: 1.2rem;
        }

        .bq-new-account-card p {
            margin: 0 0 18px;
            color: #98a2ff;
            font-size: 0.95rem;
        }

        .bq-new-form {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
        }

        .bq-new-form .bq-input,
        .bq-new-form select {
            flex: 1 1 220px;
        }

        .bq-new-form select {
            border-radius: 18px;
            border: none;
            padding: 12px 14px;
            font-size: 1rem;
        }

        .bq-new-form .bq-search-button {
            padding: 12px 28px;
        }

        .bq-accounts-list {
            display: flex;
            flex-direction: column;
            gap: 18px;
        }

        .bq-account-card {
            border-radius: 30px;
            padding: 22px;
            background: linear-gradient(110deg, #ffffff, #eef2ff);
            box-shadow: 0 25px 50px rgba(7,16,54,0.2);
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 18px;
        }

        .bq-account-info {
            display: flex;
            gap: 16px;
            align-items: center;
        }

        .bq-account-avatar {
            width: 56px;
            height: 56px;
            border-radius: 18px;
            background: #1d4ed8;
            color: #fff;
            font-weight: 700;
            font-size: 1.3rem;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .bq-account-details h3 {
            margin: 0;
            font-size: 1.1rem;
            color: #0f172a;
        }

        .bq-account-details p {
            margin: 1px 0;
            color: #4b5563;
            font-size: 0.95rem;
        }

        .bq-account-meta {
            min-width: 180px;
            text-align: right;
        }

        .bq-account-meta .bq-balance {
            margin: 0 0 6px;
            font-size: 1.2rem;
            color: #16a34a;
            font-weight: 700;
        }

        .bq-account-meta a {
            color: #2563eb;
            text-decoration: none;
            font-weight: 600;
        }

        .bq-empty-state {
            color: #e0e7ff;
            text-align: center;
            margin-top: 16px;
        }

        .alert-success {
            background: #dcfce7;
            color: #15803d;
            padding: 12px 18px;
            border-radius: 18px;
            margin-top: 12px;
            font-weight: 600;
        }

        @media (max-width: 640px) {
            .bq-account-card {
                flex-direction: column;
                align-items: flex-start;
            }

            .bq-account-meta {
                text-align: left;
            }
        }
    </style>
</head>
<body class="bg-gradient bq-accounts">
<div class="bq-accounts-wrapper">

    <header class="bq-accounts-header">
        <a class="bq-accounts-back" href="<%=request.getContextPath()%>/banquito/home">
            <span class="material-icons-round">arrow_back</span>
        </a>
        <div class="bq-accounts-header-text">
            <h1>Cuentas del Cliente</h1>
            <p>Consultar cuentas por cÃ©dula</p>
        </div>
    </header>

    <section class="bq-search-card">
        <p style="color:#d1ddff;margin:0 0 8px;font-weight:600;">Buscar cuentas</p>
        <p style="color:#9cb6ff;margin:0 0 18px;">Ingrese la cÃ©dula del cliente para listar sus cuentas.</p>
        <form method="get" action="cuentas">
            <div class="bq-search-field">
                <div class="bq-input">
                    <span class="material-icons-round" style="color:#2563eb;">search</span>
                    <input type="text"
                           name="cedula"
                           placeholder="CÃ©dula del cliente"
                           value="<%= cedulaBuscada != null ? cedulaBuscada : "" %>">
                </div>
                <button type="submit" class="bq-search-button">
                    Buscar
                </button>
            </div>
        </form>
        <% if (error != null) { %>
        <div class="alert-error" style="margin-top:16px;"><%= error %></div>
        <% } %>
    </section>

    <section class="bq-new-account-card">
        <h2>Registrar nueva cuenta</h2>
        <p>Permite abrir cuentas adicionales para el cliente seleccionado.</p>
        <% if (nuevaCuentaError != null) { %>
        <div class="alert-error"><%= nuevaCuentaError %></div>
        <% } %>
        <% if (nuevaCuentaExito != null) { %>
        <div class="alert-success"><%= nuevaCuentaExito %></div>
        <% } %>
        <form class="bq-new-form" method="post" action="cuentas">
            <div class="bq-input">
                <span class="material-icons-round" style="color:#22d3ee;">badge</span>
                <input type="text"
                       name="nuevaCuentaCedula"
                       placeholder="Cédula del cliente"
                       value="<%= nuevaCuentaCedula != null ? nuevaCuentaCedula : "" %>">
            </div>
            <select name="nuevaCuentaTipo">
                <option value="">Tipo de cuenta</option>
                <option value="AHORROS" <%= "AHORROS".equalsIgnoreCase(nuevaCuentaTipo != null ? nuevaCuentaTipo : "") ? "selected" : "" %>>
                    Ahorros
                </option>
                <option value="CORRIENTE" <%= "CORRIENTE".equalsIgnoreCase(nuevaCuentaTipo != null ? nuevaCuentaTipo : "") ? "selected" : "" %>>
                    Corriente
                </option>
            </select>
            <div class="bq-input">
                <span class="material-icons-round" style="color:#22d3ee;">attach_money</span>
                <input type="text"
                       name="nuevaCuentaSaldo"
                       placeholder="Saldo inicial (opcional)"
                       value="<%= nuevaCuentaSaldo != null ? nuevaCuentaSaldo : "" %>">
            </div>
            <button type="submit" class="bq-search-button">
                Crear cuenta
            </button>
        </form>
    </section>

    <% if (cuentas != null && !cuentas.isEmpty()) { %>
    <section class="bq-accounts-list">
        <% for (CuentaDTO c : cuentas) { %>
        <article class="bq-account-card">
            <div class="bq-account-info">
                <div class="bq-account-avatar">
                    <%= c.nombreCliente != null && !c.nombreCliente.isBlank()
                            ? c.nombreCliente.substring(0,1).toUpperCase()
                            : "C" %>
                </div>
                <div class="bq-account-details">
                    <h3><%= c.nombreCliente %></h3>
                    <p>CI: <%= c.cedulaCliente %></p>
                    <p>N&ordm; Cuenta: <%= c.numCuenta %></p>
                    <p>Tipo: <%= c.tipoCuenta %></p>
                </div>
            </div>
            <div class="bq-account-meta">
                <p class="bq-balance">
                    $<%= String.format("%,.2f", c.saldo) %>
                </p>
                <a href="<%=request.getContextPath()%>/banquito/movimientos?cuenta=<%= c.numCuenta %>">
                    Ver movimientos &rarr;
                </a>
            </div>
        </article>
        <% } %>
    </section>
    <% } else if (cedulaBuscada != null && !cedulaBuscada.isBlank()) { %>
    <p class="bq-empty-state">
        No se encontraron cuentas para la cÃ©dula indicada.
    </p>
    <% } else { %>
    <p class="bq-empty-state">
        Busque una cÃ©dula para ver las cuentas.
    </p>
    <% } %>
</div>
</body>
</html>










