<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Panel principal</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        :root {
            --bq-blue-top: #0c5adf;
            --bq-blue-bottom: #0e3b9f;
            --bq-card-bg: #ffffff;
            --bq-text-main: #ffffff;
            --shadow-soft: 0 18px 40px rgba(0, 0, 0, 0.35);
            --radius-card: 28px;
            --transition-fast: 0.18s ease-out;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background: linear-gradient(to bottom, var(--bq-blue-top), var(--bq-blue-bottom));
            min-height: 100vh;
            color: var(--bq-text-main);
        }

        .page-wrapper {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        /* HEADER SUPERIOR: BanQuito + bienvenida + botón salir */
        .top-bar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 16px 28px;
        }

        .top-left {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .avatar-circle {
            width: 44px;
            height: 44px;
            border-radius: 50%;
            background: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 10px 24px rgba(0,0,0,0.35);
            overflow: hidden;
        }

        .avatar-circle span {
            font-weight: 700;
            color: #0d5adf;
            font-size: 22px;
        }

        .top-text {
            display: flex;
            flex-direction: column;
        }

        .top-text-title {
            font-weight: 700;
            font-size: 22px;
            letter-spacing: 0.02em;
        }

        .top-text-sub {
            font-size: 13px;
            opacity: 0.9;
        }

        .logout-btn {
            border-radius: 999px;
            border: 2px solid rgba(255,255,255,0.85);
            padding: 6px 16px;
            background: transparent;
            color: #fff;
            font-size: 13px;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            cursor: pointer;
            transition: background var(--transition-fast), color var(--transition-fast);
        }

        .logout-btn span {
            font-size: 16px;
        }

        .logout-btn:hover {
            background: rgba(255,255,255,0.14);
        }

        /* CONTENIDO PRINCIPAL */
        .content {
            flex: 1;
            padding: 0 24px 40px;
            display: flex;
            justify-content: center;
        }

        .main-card {
            width: 100%;
            max-width: 1080px;
            background: #f8f9ff;
            border-radius: 38px 38px 0 0;
            margin-top: 6px;
            box-shadow: 0 26px 60px rgba(0,0,0,0.45);
            padding: 32px 30px 40px;
            position: relative;
            overflow: hidden;
        }

        .main-card::before {
            content: "";
            position: absolute;
            inset: 0;
            background: linear-gradient(to bottom, rgba(255,255,255,0.85), rgba(255,255,255,0.94));
            z-index: 0;
        }

        .main-inner {
            position: relative;
            z-index: 1;
        }

        .section-header {
            margin-bottom: 26px;
        }

        .section-title {
            font-size: 26px;
            font-weight: 800;
            color: #113366;
            margin-bottom: 4px;
        }

        .section-subtitle {
            font-size: 14px;
            color: #6f7b92;
            margin-bottom: 2px;
        }

        .section-subtitle span {
            font-weight: 600;
            color: #3d4f9f;
        }

        /* GRID DE MÓDULOS */
        .module-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
            gap: 22px;
        }

        .module-card {
            border-radius: var(--radius-card);
            padding: 22px 22px 20px;
            color: #fff;
            box-shadow: var(--shadow-soft);
            position: relative;
            overflow: hidden;
            display: flex;
            flex-direction: column;
            min-height: 150px;
            cursor: pointer;
            text-decoration: none;
            transition: transform var(--transition-fast), box-shadow var(--transition-fast);
        }

        .module-card::after {
            content: "";
            position: absolute;
            inset: 0;
            background: radial-gradient(circle at 0 0, rgba(255,255,255,0.26), transparent 55%);
            opacity: 0.9;
            pointer-events: none;
        }

        .module-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 22px 44px rgba(0,0,0,0.5);
        }

        .module-top {
            display: flex;
            align-items: center;
            gap: 14px;
            margin-bottom: 14px;
        }

        .module-icon {
            width: 48px;
            height: 48px;
            border-radius: 18px;
            background: rgba(255,255,255,0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            font-weight: 700;
        }

        .module-text-title {
            font-size: 20px;
            font-weight: 800;
            letter-spacing: 0.02em;
        }

        .module-text-sub {
            font-size: 13px;
            opacity: 0.92;
        }

        .module-bottom {
            margin-top: auto;
            display: flex;
            justify-content: flex-end;
            align-items: center;
            gap: 6px;
            font-size: 14px;
            font-weight: 600;
        }

        .module-bottom span:last-child {
            font-size: 18px;
        }

        /* Colores de tarjetas */
        .mod-clientes      { background: linear-gradient(135deg, #1959d8, #0943b6); }
        .mod-nuevo-cliente { background: linear-gradient(135deg, #1b9652, #08783e); }
        .mod-cuentas       { background: linear-gradient(135deg, #6e2bb6, #4a1792); }
        .mod-movimientos   { background: linear-gradient(135deg, #f36c1f, #df3a10); }
        .mod-creditos      { background: linear-gradient(135deg, #c53030, #9b1515); }
        .mod-cuotas        { background: linear-gradient(135deg, #007c8c, #00556a); }

        /* FOOTER */
        .footer {
            text-align: center;
            padding-top: 24px;
            font-size: 12px;
            color: #8aa1ff;
        }

        .footer-logo {
            margin-top: 18px;
            font-weight: 800;
            letter-spacing: 0.08em;
            font-size: 13px;
            color: #2b3f93;
        }

        @media (max-width: 768px) {
            .main-card {
                border-radius: 28px 28px 0 0;
                padding: 22px 18px 28px;
            }

            .top-bar {
                padding: 14px 16px;
            }

            .section-title {
                font-size: 22px;
            }
        }
    </style>
</head>
<body>
<div class="page-wrapper">

    <!-- Barra superior -->
    <header class="top-bar">
        <div class="top-left">
            <div class="avatar-circle">
                <!-- Puedes reemplazar la letra por una imagen <img> -->
                <span>B</span>
            </div>
            <div class="top-text">
                <div class="top-text-title">BanQuito</div>
                <div class="top-text-sub">Bienvenido, MONSTER</div>
            </div>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/logout" style="margin:0;">
            <!-- De momento solo es visual; luego conectamos este logout -->
            <button type="button" class="logout-btn">
                <span>&#11104;</span>
                Salir
            </button>
        </form>
    </header>

    <!-- Contenido principal -->
    <main class="content">
        <section class="main-card">
            <div class="main-inner">

                <div class="section-header">
                    <div class="section-title">Módulos principales</div>
                    <div class="section-subtitle">
                        Gestiona <span>clientes, cuentas, créditos</span> y más.
                    </div>
                    <div class="section-subtitle">
                        Selecciona una opción para continuar.
                    </div>
                </div>

                <div class="module-grid">
                    <!-- CLIENTES -->
                    <a class="module-card mod-clientes"
                       href="${pageContext.request.contextPath}/banquito/clientes">
                        <div class="module-top">
                            <div class="module-icon">C</div>
                            <div>
                                <div class="module-text-title">Clientes</div>
                                <div class="module-text-sub">
                                    Lista de clientes y búsqueda por cédula.
                                </div>
                            </div>
                        </div>
                        <div class="module-bottom">
                            <span>Ver clientes</span>
                            <span>&rarr;</span>
                        </div>
                    </a>

                    <!-- NUEVO CLIENTE -->
                    <a class="module-card mod-nuevo-cliente"
                       href="${pageContext.request.contextPath}/banquito/clientes/nuevo">
                        <div class="module-top">
                            <div class="module-icon">+</div>
                            <div>
                                <div class="module-text-title">Nuevo Cliente</div>
                                <div class="module-text-sub">
                                    Registro de un nuevo cliente BanQuito.
                                </div>
                            </div>
                        </div>
                        <div class="module-bottom">
                            <span>Registrar</span>
                            <span>&rarr;</span>
                        </div>
                    </a>

                    <!-- CUENTAS -->
                    <a class="module-card mod-cuentas"
                       href="${pageContext.request.contextPath}/banquito/cuentas">
                        <div class="module-top">
                            <div class="module-icon">&#x1F3E6;</div>
                            <div>
                                <div class="module-text-title">Cuentas</div>
                                <div class="module-text-sub">
                                    Consulta de todas las cuentas y saldos.
                                </div>
                            </div>
                        </div>
                        <div class="module-bottom">
                            <span>Ver cuentas</span>
                            <span>&rarr;</span>
                        </div>
                    </a>

                    <!-- MOVIMIENTOS -->
                    <a class="module-card mod-movimientos"
                       href="${pageContext.request.contextPath}/banquito/movimientos">
                        <div class="module-top">
                            <div class="module-icon">&#x21C6;</div>
                            <div>
                                <div class="module-text-title">Movimientos</div>
                                <div class="module-text-sub">
                                    Depósitos, retiros y transferencias.
                                </div>
                            </div>
                        </div>
                        <div class="module-bottom">
                            <span>Ver movimientos</span>
                            <span>&rarr;</span>
                        </div>
                    </a>

                    <!-- CRÉDITOS -->
                    <a class="module-card mod-creditos"
                       href="<%= request.getContextPath() %>/banquito/creditos/evaluar">
                        <div class="module-top">
                            <div class="module-icon">&#x1F4B3;</div>
                            <div>
                                <div class="module-text-title">Créditos</div>
                                <div class="module-text-sub">
                                    Evaluación de créditos y sujeto de crédito.
                                </div>
                            </div>
                        </div>
                        <div class="module-bottom">
                            <span>Gestionar créditos</span>
                            <span>&rarr;</span>
                        </div>
                    </a>

                    <!-- CUOTAS -->
                    <a class="module-card mod-cuotas"
                       href="<%= request.getContextPath() %>/banquito/creditos/cuotas">
                        <div class="module-top">
                            <div class="module-icon">&#x1F4DD;</div>
                            <div>
                                <div class="module-text-title">Cuotas</div>
                                <div class="module-text-sub">
                                    Consulta de cuotas de amortización.
                                </div>
                            </div>
                        </div>
                        <div class="module-bottom">
                            <span>Ver cuotas</span>
                            <span>&rarr;</span>
                        </div>
                    </a>
                </div>

                <div class="footer">
                    © 2025 BanQuito &amp; ElectroQuito
                    <div class="footer-logo">BANQUITO</div>
                </div>

            </div>
        </section>
    </main>

</div>
</body>
</html>
