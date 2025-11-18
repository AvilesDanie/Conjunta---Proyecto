<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.dto.MovimientoDTO"%>

<%
    @SuppressWarnings("unchecked")
    List<MovimientoDTO> movimientos =
        (List<MovimientoDTO>) request.getAttribute("movimientos");
    String numeroCuenta = (String) request.getAttribute("numeroCuenta");
    String error = (String) request.getAttribute("error");
    String exito = (String) request.getAttribute("exito");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Movimientos</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet">
    <style>
        body.bq-mov-body {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', 'Segoe UI', Arial, sans-serif;
            background: linear-gradient(180deg, #0065d6 0%, #1d4ed8 30%, #141867 30%, #0f143d 100%);
            display: block;
            padding: 0 0 60px;
        }

        .bq-mov-wrapper {
            width: min(880px, 100%);
            margin: 0 auto;
            padding: 28px 18px 0;
            display: flex;
            flex-direction: column;
            gap: 24px;
        }

        .bq-mov-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            color: #fff;
        }

        .bq-mov-header-info {
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .bq-mov-back {
            border: none;
            width: 48px;
            height: 48px;
            border-radius: 16px;
            background: rgba(255,255,255,0.18);
            color: #fff;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
        }

        .bq-mov-title {
            margin: 0;
            font-size: 1.9rem;
            font-weight: 700;
        }

        .bq-mov-subtitle {
            margin: 4px 0 0;
            font-size: 1rem;
            opacity: 0.9;
        }

        .bq-mov-search {
            border-radius: 26px;
            padding: 20px;
            background: linear-gradient(180deg, #18226e, #151a53);
            box-shadow: 0 20px 45px rgba(3, 4, 24, 0.55);
        }

        .bq-mov-search form {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .bq-mov-search .bq-input {
            flex: 1 1 240px;
            display: flex;
            align-items: center;
            background: #fff;
            border-radius: 18px;
            padding: 0 12px;
        }

        .bq-mov-search input {
            border: none;
            outline: none;
            width: 100%;
            padding: 12px 8px;
            font-size: 1rem;
            font-family: inherit;
        }

        .bq-mov-search button {
            border: none;
            border-radius: 999px;
            background: linear-gradient(120deg, #22d3ee, #2563eb);
            color: #fff;
            font-weight: 600;
            padding: 14px 28px;
            cursor: pointer;
            box-shadow: 0 15px 30px rgba(34, 211, 238, 0.25);
        }

        .bq-mov-action {
            border-radius: 26px;
            padding: 20px;
            background: linear-gradient(180deg, #101344, #0c102f);
            box-shadow: 0 20px 45px rgba(3, 4, 24, 0.45);
        }

        .bq-mov-action h2 {
            margin: 0 0 12px;
            color: #d8e1ff;
        }

        .bq-mov-form {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            margin-bottom: 18px;
        }

        .bq-mov-form select,
        .bq-mov-form input {
            flex: 1 1 220px;
            border-radius: 18px;
            border: none;
            padding: 12px 14px;
            font-size: 1rem;
        }

        .bq-mov-form button {
            border: none;
            border-radius: 999px;
            padding: 12px 28px;
            font-weight: 600;
            color: #fff;
            background: linear-gradient(120deg, #34d399, #059669);
            cursor: pointer;
            box-shadow: 0 14px 26px rgba(5, 150, 105, 0.35);
        }

        .bq-divider {
            border-top: 1px solid rgba(255,255,255,0.08);
            margin: 18px 0;
        }

        .bq-mov-list {
            display: flex;
            flex-direction: column;
            gap: 18px;
        }

        .bq-mov-card {
            border-radius: 26px;
            padding: 18px 20px;
            background: linear-gradient(120deg, #ffffff, #eef2ff);
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 18px 35px rgba(4, 6, 28, 0.35);
        }

        .bq-mov-icon {
            width: 56px;
            height: 56px;
            border-radius: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.6rem;
            margin-right: 18px;
        }

        .bq-mov-info {
            display: flex;
            align-items: center;
            gap: 18px;
        }

        .bq-mov-details h3 {
            margin: 0;
            font-size: 1.1rem;
            color: #0f172a;
        }

        .bq-mov-details p {
            margin: 4px 0 0;
            color: #6b7280;
            font-size: 0.95rem;
        }

        .bq-mov-amount {
            font-size: 1.2rem;
            font-weight: 700;
        }

        .mov-ingreso .bq-mov-icon {
            background: #d9fbe1;
            color: #16a34a;
        }

        .mov-egreso .bq-mov-icon {
            background: #fee2e2;
            color: #b91c1c;
        }

        .mov-transfer .bq-mov-icon {
            background: #dbeafe;
            color: #2563eb;
        }

        .mov-ingreso .bq-mov-amount {
            color: #16a34a;
        }

        .mov-egreso .bq-mov-amount {
            color: #ef4444;
        }

        .mov-transfer .bq-mov-amount {
            color: #2563eb;
        }

        .bq-empty-state,
        .alert-error,
        .alert-success {
            border-radius: 18px;
        }

        .alert-success {
            background: #dcfce7;
            color: #15803d;
            padding: 12px 18px;
            font-weight: 600;
        }

        @media (max-width: 640px) {
            .bq-mov-card {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body class="bg-gradient bq-mov-body">
<div class="bq-mov-wrapper">

    <header class="bq-mov-header">
        <div class="bq-mov-header-info">
            <a class="bq-mov-back" href="<%=request.getContextPath()%>/banquito/cuentas">
                <span class="material-icons-round">arrow_back</span>
            </a>
            <div>
                <p class="bq-mov-title">Movimientos</p>
                <p class="bq-mov-subtitle">
                    <%= numeroCuenta != null && !numeroCuenta.isBlank()
                            ? "Cuenta " + numeroCuenta
                            : "Busca una cuenta para ver sus movimientos" %>
                </p>
            </div>
        </div>
    </header>

    <section class="bq-mov-search">
        <p style="color:#d8e1ff;margin:0 0 12px;">Ingresa el nÃºmero de cuenta para consultar movimientos.</p>
        <form method="get" action="<%=request.getContextPath()%>/banquito/movimientos">
            <div class="bq-input">
                <input type="text"
                       name="cuenta"
                       placeholder="CTA-0001-2024"
                       value="<%= numeroCuenta != null ? numeroCuenta : "" %>">
            </div>
            <button type="submit">Buscar</button>
        </form>
    </section>

    <% if (error != null) { %>
    <div class="alert-error"><%= error %></div>
    <% } %>
    <% if (exito != null) { %>
    <div class="alert-success"><%= exito %></div>
    <% } %>

    <section class="bq-mov-action">
        <h2>Depósito / Retiro</h2>
        <form class="bq-mov-form" method="post" action="<%=request.getContextPath()%>/banquito/movimientos">
            <select name="movimientoTipo">
                <option value="DEP">Depósito</option>
                <option value="RET">Retiro</option>
            </select>
            <input type="text"
                   name="cuentaSimple"
                   placeholder="Número de cuenta"
                   value="<%= numeroCuenta != null ? numeroCuenta : "" %>">
            <input type="text"
                   name="montoSimple"
                   placeholder="Monto (USD)">
            <input type="date"
                   name="fechaSimple">
            <button type="submit">Registrar</button>
        </form>

        <div class="bq-divider"></div>

        <h2>Transferencia</h2>
        <form class="bq-mov-form" method="post" action="<%=request.getContextPath()%>/banquito/movimientos">
            <input type="hidden" name="movimientoTipo" value="TRA">
            <input type="text"
                   name="transferOrigen"
                   placeholder="Cuenta origen">
            <input type="text"
                   name="transferDestino"
                   placeholder="Cuenta destino">
            <input type="text"
                   name="transferMonto"
                   placeholder="Monto (USD)">
            <input type="date"
                   name="transferFecha">
            <button type="submit">Transferir</button>
        </form>
    </section>

    <section class="bq-mov-list">
        <% if (movimientos != null && !movimientos.isEmpty()) {
            for (MovimientoDTO m : movimientos) {
                boolean esIngreso = "DEPOSITO".equalsIgnoreCase(m.tipo) || "DEP".equalsIgnoreCase(m.tipo);
                boolean esTransfer = "TRANSFERENCIA".equalsIgnoreCase(m.tipo) || "TRA".equalsIgnoreCase(m.tipo);
                String cardClass = esIngreso ? "mov-ingreso" : (esTransfer ? "mov-transfer" : "mov-egreso");
                String signo = esIngreso ? "+" : "-";
        %>
        <article class="bq-mov-card <%= cardClass %>">
            <div class="bq-mov-info">
                <div class="bq-mov-icon">
                    <span class="material-icons-round">
                        <%= esIngreso ? "south_west" : (esTransfer ? "sync_alt" : "north_east") %>
                    </span>
                </div>
                <div class="bq-mov-details">
                    <h3><%= m.tipo %></h3>
                    <p><%= m.fecha %></p>
                </div>
            </div>
            <div class="bq-mov-amount">
                <%= signo %>$<%= String.format("%,.2f", m.valor) %>
            </div>
        </article>
        <% } } else if (numeroCuenta != null && (error == null || error.isBlank())) { %>
        <p class="bq-empty-state">No hay movimientos registrados para esta cuenta.</p>
        <% } %>
    </section>
</div>
</body>
</html>

