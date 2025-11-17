<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-gradient">
    <div class="app-container">

        <!-- Flecha de regreso -->
        <a href="${pageContext.request.contextPath}/index.jsp" class="back-link">
            <span>&larr;</span> Atrás
        </a>

        <!-- Cabecera de login (logo + textos) -->
        <div class="login-header">
            <div class="login-logo-circle">
                <!-- Luego puedes poner aquí la imagen real -->
                <span class="login-logo-text">BanQuito</span>
            </div>
            <div class="login-main-title">BanQuito</div>
            <div class="login-main-subtitle">Sistema Bancario</div>
        </div>

        <!-- Tarjeta de formulario -->
        <div class="login-card">

            <%
                String error = (String) request.getAttribute("error");
                String usernameValor = (String) request.getAttribute("username");
                if (error != null) {
            %>
                <div class="alert-error"><%= error %></div>
            <% } %>

            <!-- OJO: action con contextPath y names que el servlet espera -->
            <form action="${pageContext.request.contextPath}/banquito/login" method="post">
                <div class="form-group">
                    <label class="form-label" for="username">Usuario</label>
                    <input class="form-control"
                           type="text"
                           id="username"
                           name="username"
                           value="<%= usernameValor != null ? usernameValor : "" %>"
                           required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Contraseña</label>
                    <input class="form-control"
                           type="password"
                           id="password"
                           name="password"
                           required>
                </div>

                <button type="submit" class="btn-primary">INGRESAR</button>
            </form>
        </div>
    </div>
</body>
</html>
