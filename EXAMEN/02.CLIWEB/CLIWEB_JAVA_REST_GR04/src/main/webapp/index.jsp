<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito & ElectroQuito</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body class="bg-gradient">
    <div class="welcome-wrapper">

        <!-- Encabezado tipo app móvil -->
        <div class="welcome-header">
            <div class="welcome-logo-circle">
                <!-- Icono edificio banco -->
                <span class="welcome-logo-icon">&#127974;</span>
            </div>

            <h1 class="welcome-title">¡Bienvenido!</h1>
            <p class="welcome-subtitle-main">Sistema Integrado de Gestión</p>
            <p class="welcome-subtitle-secondary">Selecciona tu aplicación</p>
        </div>

        <!-- Tarjetas de selección -->
        <div class="app-options">
            <a href="banquitoLogin.jsp" class="app-card app-card-banquito">
                <div class="app-card-icon">
                    <!-- banco -->
                    <span class="app-card-icon-symbol">&#127974;</span>
                </div>
                <div class="app-card-text">
                    <div class="app-card-text-title">BanQuito</div>
                    <div class="app-card-text-subtitle">Sistema Bancario Integral</div>
                </div>
                <div class="app-card-arrow">→</div>
            </a>

            <a href="electroquitoLogin.jsp" class="app-card app-card-electro">
                <div class="app-card-icon">
                    <!-- tienda / comercio -->
                    <span class="app-card-icon-symbol">&#127970;</span>
                </div>
                <div class="app-card-text">
                    <div class="app-card-text-title">ElectroQuito</div>
                    <div class="app-card-text-subtitle">Comercializadora Digital</div>
                </div>
                <div class="app-card-arrow">→</div>
            </a>
        </div>

        <!-- Footer -->
        <div class="app-footer">
            © 2025 BanQuito &amp; ElectroQuito
        </div>
    </div>
</body>
</html>
