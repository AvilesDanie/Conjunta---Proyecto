<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="ec.edu.monster.dto.CuentaDTO"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Cuentas del Cliente</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    <style>
        body.client-accounts {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', 'Segoe UI', Arial, sans-serif;
            background: radial-gradient(circle at top, #0ea5e9 0%, #2563eb 35%, #0f172a 100%);
            color: #fff;
            display: flex;
            justify-content: center;
            padding: 48px 18px 60px;
        }

        .client-accounts-wrapper {
            width: min(980px, 100%);
            display: flex;
            flex-direction: column;
            gap: 32px;
        }

        .client-accounts-header {
            display: flex;
            gap: 18px;
            align-items: center;
        }

        .client-back-btn {
            width: 48px;
            height: 48px;
            border-radius: 16px;
            background: rgba(255,255,255,0.16);
            color: #fff;
            font-size: 1.5rem;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 18px 35px rgba(15,23,42,0.45);
        }

        .client-title {
            margin: 0;
            font-size: clamp(1.8rem, 4vw, 2.4rem);
            font-weight: 700;
        }

        .client-subtitle {
            margin: 4px 0 0;
            opacity: 0.9;
            font-size: 1rem;
        }

        .client-account-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 22px;
        }

        .client-account-card {
            border-radius: 32px;
            padding: 22px 24px;
            background: linear-gradient(135deg, rgba(255,255,255,0.18), rgba(255,255,255,0.04));
            backdrop-filter: blur(16px);
            box-shadow: 0 30px 60px rgba(5,10,30,0.45);
        }

        .client-account-card h3 {
            margin: 0;
            font-size: 1.1rem;
            letter-spacing: 0.05em;
        }

        .account-type {
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            margin: 6px 0 10px;
            opacity: 0.9;
        }

        .account-divider {
            height: 1px;
            background: rgba(255,255,255,0.25);
            margin: 16px 0;
        }

        .account-balance-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 1rem;
        }

        .account-balance {
            font-size: 1.3rem;
            font-weight: 600;
            color: #bbf7d0;
        }

        .account-mov-link {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            margin-top: 18px;
            width: 100%;
            border-radius: 16px;
            padding: 10px 16px;
            background: linear-gradient(120deg, #22d3ee, #0ea5e9);
            color: #0f172a;
            font-weight: 600;
            text-decoration: none;
            transition: transform 0.18s ease, box-shadow 0.18s ease;
            box-shadow: 0 16px 30px rgba(14,165,233,0.35);
        }

        .account-mov-link:hover {
            transform: translateY(-2px);
            box-shadow: 0 22px 35px rgba(14,165,233,0.45);
        }

        .empty-text {
            text-align: center;
            background: rgba(15,23,42,0.35);
            border-radius: 24px;
            padding: 18px;
            font-size: 1rem;
            color: #e0e7ff;
        }

        @media (max-width: 620px) {
            .client-accounts-header {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body class="client-accounts">
<div class="client-accounts-wrapper">

    <header class="client-accounts-header">
        <a href="<%=request.getContextPath()%>/banquito/clientes" class="client-back-btn">
            &#8592;
        </a>
        <div>
            <p class="client-title">Cuentas del Cliente</p>
            <p class="client-subtitle">
                <%= request.getAttribute("nombre") != null ? request.getAttribute("nombre") : "" %>
            </p>
        </div>
    </header>

    <main>

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

        <section class="client-account-grid">
            <%
                for (CuentaDTO cta : cuentas) {
                    String saldoStr = nf != null ? nf.format(cta.saldo).replace(" ", "") : String.valueOf(cta.saldo);
            %>
            <article class="client-account-card">
                <h3><%= cta.numCuenta %></h3>
                <p class="account-type">Tipo: <%= cta.tipoCuenta %></p>

                <p style="margin:0;font-size:0.95rem;opacity:0.84;">
                    <%= cta.nombreCliente != null ? cta.nombreCliente : "" %>
                </p>

                <div class="account-divider"></div>

                <div class="account-balance-row">
                    <span>Saldo</span>
                    <span class="account-balance"><%= saldoStr %></span>
                </div>

                <a class="account-mov-link"
                   href="<%=request.getContextPath()%>/banquito/movimientos?cuenta=<%= URLEncoder.encode(cta.numCuenta, "UTF-8") %>">
                    Ver movimientos
                </a>
            </article>
            <% } %>
        </section>

        <% } %>
    </main>
</div>
</body>
</html>
