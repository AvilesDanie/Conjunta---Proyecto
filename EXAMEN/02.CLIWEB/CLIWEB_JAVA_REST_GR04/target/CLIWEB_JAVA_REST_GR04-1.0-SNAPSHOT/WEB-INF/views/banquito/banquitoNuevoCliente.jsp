<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String cedula = (String) request.getAttribute("cedula");
    String nombre = (String) request.getAttribute("nombre");
    String fechaNacimiento = (String) request.getAttribute("fechaNacimiento");
    String estadoCivil = (String) request.getAttribute("estadoCivil");
    String tipoCuentaInicial = (String) request.getAttribute("tipoCuentaInicial");
    String error = (String) request.getAttribute("error");
    String exito = (String) request.getAttribute("exito");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Nuevo Cliente</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet">
    <style>
        body.bnc-body {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', 'Segoe UI', Arial, sans-serif;
            background: linear-gradient(180deg, #0f5ccf 0%, #1d7be9 55%, #e0f2ff 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px 12px 48px;
        }

        .bnc-container {
            width: min(960px, 100%);
            display: flex;
            flex-direction: column;
            gap: 22px;
            color: #0f172a;
        }

        .bnc-hero {
            display: flex;
            align-items: center;
            gap: 18px;
            color: #fff;
        }

        .bnc-back-btn {
            width: 46px;
            height: 46px;
            border-radius: 16px;
            border: none;
            background: rgba(255,255,255,0.18);
            color: #fff;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 22px;
            text-decoration: none;
        }

        .bnc-hero-text {
            display: flex;
            flex-direction: column;
        }

        .bnc-hero-title {
            margin: 0;
            font-size: 1.85rem;
            font-weight: 700;
        }

        .bnc-hero-subtitle {
            margin: 6px 0 0;
            font-size: 1rem;
            opacity: 0.95;
        }

        .bnc-info-card,
        .bnc-form-card {
            background: rgba(255,255,255,0.92);
            border-radius: 26px;
            padding: 26px 32px;
            box-shadow: 0 30px 60px rgba(15, 23, 42, 0.15);
        }

        .bnc-info-title {
            margin: 0 0 6px;
            font-size: 1.25rem;
            font-weight: 600;
            color: #0f172a;
        }

        .bnc-info-text {
            margin: 0;
            color: #475569;
            line-height: 1.5;
        }

        .bnc-pill-row {
            margin-top: 16px;
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .bnc-pill {
            padding: 6px 16px;
            border-radius: 999px;
            background: #e0edff;
            color: #0f5ccf;
            font-size: 0.88rem;
            font-weight: 600;
        }

        .bnc-form-card form {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
            gap: 18px 24px;
        }

        .bnc-field {
            display: flex;
            flex-direction: column;
        }

        .bnc-label {
            font-size: 0.9rem;
            font-weight: 600;
            color: #1e3a8a;
            margin-bottom: 8px;
        }

        .bnc-input,
        .bnc-select {
            display: flex;
            align-items: center;
            border-radius: 18px;
            border: 1px solid #c7d7f5;
            background: #fff;
            box-shadow: inset 0 1px 0 rgba(15, 23, 42, 0.04);
        }

        .bnc-input span,
        .bnc-select span {
            color: #0f5ccf;
            padding-left: 14px;
        }

        .bnc-input input,
        .bnc-select select {
            flex: 1;
            border: none;
            background: transparent;
            padding: 12px 16px;
            font-size: 1rem;
            font-family: inherit;
            outline: none;
            color: #0f172a;
        }

        .bnc-alert {
            grid-column: 1 / -1;
            border-radius: 20px;
            padding: 14px 18px;
            font-weight: 600;
        }

        .bnc-alert-error {
            background: #fee2e2;
            color: #b91c1c;
        }

        .bnc-alert-success {
            background: #dcfce7;
            color: #15803d;
        }

        .bnc-submit {
            grid-column: 1 / -1;
            border: none;
            border-radius: 999px;
            background: linear-gradient(90deg, #0f5ccf, #2563eb);
            color: #fff;
            padding: 16px 30px;
            font-size: 1rem;
            font-weight: 700;
            cursor: pointer;
            box-shadow: 0 18px 32px rgba(15, 92, 207, 0.35);
        }

        .bnc-submit:hover {
            transform: translateY(-1px);
        }

        @media (max-width: 600px) {
            .bnc-hero {
                flex-direction: column;
                align-items: flex-start;
            }

            .bnc-form-card form {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body class="bnc-body">
<div class="bnc-container">

    <header class="bnc-hero">
        <a class="bnc-back-btn"
           href="<%=request.getContextPath()%>/banquito/home">
            <span class="material-icons-round">arrow_back</span>
        </a>
        <div class="bnc-hero-text">
            <p class="bnc-hero-title">Registrar Cliente</p>
            <p class="bnc-hero-subtitle">
                Ingrese los datos del nuevo cliente BanQuito.
            </p>
        </div>
    </header>

    <section class="bnc-info-card">
        <p class="bnc-info-title">Información del Cliente</p>
        <p class="bnc-info-text">
            Complete el formulario para registrar un nuevo cliente y crear su cuenta inicial
            en BanQuito. Toda la información es requerida para cumplir con políticas KYC.
        </p>
        <div class="bnc-pill-row">
            <span class="bnc-pill">Identificación</span>
            <span class="bnc-pill">Datos personales</span>
            <span class="bnc-pill">Cuenta inicial</span>
        </div>
    </section>

    <section class="bnc-form-card">
        <form method="post"
              action="<%=request.getContextPath()%>/banquito/clientes/nuevo">

            <% if (error != null) { %>
            <div class="bnc-alert bnc-alert-error"><%= error %></div>
            <% } %>

            <% if (exito != null) { %>
            <div class="bnc-alert bnc-alert-success"><%= exito %></div>
            <% } %>

            <div class="bnc-field">
                <label class="bnc-label" for="nombre">Nombres y Apellidos *</label>
                <div class="bnc-input">
                    <span class="material-icons-round">badge</span>
                    <input type="text"
                           id="nombre"
                           name="nombre"
                           placeholder="Ej. Carlos Alberto Mendoza Torres"
                           value="<%= nombre != null ? nombre : "" %>"
                           required>
                </div>
            </div>

            <div class="bnc-field">
                <label class="bnc-label" for="cedula">Cédula *</label>
                <div class="bnc-input">
                    <span class="material-icons-round">credit_card</span>
                    <input type="text"
                           id="cedula"
                           name="cedula"
                           placeholder="Ej. 0987654321"
                           value="<%= cedula != null ? cedula : "" %>"
                           required>
                </div>
            </div>

            <div class="bnc-field">
                <label class="bnc-label" for="fechaNacimiento">Fecha de Nacimiento *</label>
                <div class="bnc-input">
                    <span class="material-icons-round">calendar_today</span>
                    <input type="date"
                           id="fechaNacimiento"
                           name="fechaNacimiento"
                           value="<%= fechaNacimiento != null ? fechaNacimiento : "" %>"
                           required>
                </div>
            </div>

            <div class="bnc-field">
                <label class="bnc-label" for="estadoCivil">Estado Civil *</label>
                <div class="bnc-select">
                    <span class="material-icons-round">diversity_1</span>
                    <select id="estadoCivil" name="estadoCivil" required>
                        <option value="">Seleccione...</option>
                        <option value="SOLTERO" <%= "SOLTERO".equals(estadoCivil) ? "selected" : "" %>>Soltero</option>
                        <option value="CASADO" <%= "CASADO".equals(estadoCivil) ? "selected" : "" %>>Casado</option>
                        <option value="DIVORCIADO" <%= "DIVORCIADO".equals(estadoCivil) ? "selected" : "" %>>Divorciado</option>
                        <option value="VIUDO" <%= "VIUDO".equals(estadoCivil) ? "selected" : "" %>>Viudo</option>
                    </select>
                </div>
            </div>

            <div class="bnc-field">
                <label class="bnc-label" for="tipoCuentaInicial">Tipo de Cuenta Inicial *</label>
                <div class="bnc-select">
                    <span class="material-icons-round">account_balance</span>
                    <select id="tipoCuentaInicial" name="tipoCuentaInicial" required>
                        <option value="">Seleccione...</option>
                        <option value="AHORROS" <%= "AHORROS".equals(tipoCuentaInicial) ? "selected" : "" %>>Ahorros</option>
                        <option value="CORRIENTE" <%= "CORRIENTE".equals(tipoCuentaInicial) ? "selected" : "" %>>Corriente</option>
                    </select>
                </div>
            </div>

            <button type="submit" class="bnc-submit">
                Registrar Cliente
            </button>
        </form>
    </section>
</div>
</body>
</html>
