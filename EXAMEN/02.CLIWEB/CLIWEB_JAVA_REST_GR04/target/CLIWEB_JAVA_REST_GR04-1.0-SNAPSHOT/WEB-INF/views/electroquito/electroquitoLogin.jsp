<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="false" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>ElectroQuito - Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Fuente -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap"
          rel="stylesheet">
    <!-- Iconos -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(180deg, #f97316 0%, #fbbf24 40%, #ffffff 100%);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-container {
            width: 100%;
            max-width: 420px;
            background: #ffffff;
            border-radius: 26px;
            padding: 32px 28px 28px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
        }

        .logo-row {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 8px;
        }

        .logo-circle {
            width: 46px;
            height: 46px;
            border-radius: 50%;
            background: #fb923c;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #ffffff;
            font-size: 26px;
            font-weight: 700;
        }

        .app-name {
            font-size: 22px;
            font-weight: 700;
            color: #1f2933;
        }

        .subtitle {
            margin-top: 0;
            font-size: 13px;
            color: #6b7280;
            margin-bottom: 24px;
        }

        .field-label {
            font-size: 13px;
            color: #6b7280;
            margin-bottom: 4px;
        }

        .input-wrapper {
            position: relative;
            margin-bottom: 16px;
        }

        .input-icon {
            position: absolute;
            left: 14px;
            top: 50%;
            transform: translateY(-50%);
            color: #9ca3af;
            font-size: 20px;
        }

        .text-input {
            width: 100%;
            font-family: inherit;
            border-radius: 999px;
            padding: 10px 16px 10px 42px;
            border: 1px solid #d1d5db;
            font-size: 14px;
            outline: none;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        .text-input:focus {
            border-color: #fb923c;
            box-shadow: 0 0 0 3px rgba(251, 146, 60, 0.25);
        }

        .btn-login {
            margin-top: 12px;
            width: 100%;
            border: none;
            border-radius: 999px;
            padding: 12px 16px;
            background: #2563eb;
            color: #ffffff;
            font-weight: 600;
            font-size: 15px;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            box-shadow: 0 14px 28px rgba(37, 99, 235, 0.35);
        }

        .btn-login:hover {
            filter: brightness(1.05);
        }

        .error-box {
            margin-top: 16px;
            border-radius: 16px;
            padding: 10px 12px;
            background: #fef2f2;
            color: #991b1b;
            font-size: 13px;
            display: flex;
            align-items: flex-start;
            gap: 8px;
        }

        .error-icon {
            font-size: 18px;
        }

        .helper-text {
            margin-top: 14px;
            font-size: 12px;
            color: #9ca3af;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="login-container">
    <div class="logo-row">
        <div class="logo-circle">EQ</div>
        <div class="app-name">ElectroQuito</div>
    </div>
    <p class="subtitle">Inicie sesión para gestionar productos y facturas.</p>

    <form method="post"
          action="${pageContext.request.contextPath}/electroquito/login">

        <div class="field-label">Usuario</div>
        <div class="input-wrapper">
            <span class="material-icons input-icon">person</span>
            <!-- aquí ya no usamos fn:escapeXml -->
            <input type="text"
                   name="username"
                   class="text-input"
                   value="${username}"
                   placeholder="Ej: MONSTER">
        </div>

        <div class="field-label">Contraseña</div>
        <div class="input-wrapper">
            <span class="material-icons input-icon">lock</span>
            <input type="password"
                   name="password"
                   class="text-input"
                   placeholder="••••••••">
        </div>

        <button type="submit" class="btn-login">
            <span class="material-icons">login</span>
            <span>Ingresar</span>
        </button>

        <% if (request.getAttribute("error") != null) { %>
        <div class="error-box">
            <span class="material-icons error-icon">error_outline</span>
            <span><%= request.getAttribute("error") %></span>
        </div>
        <% } %>

        <p class="helper-text">
            Usuario demo: <strong>MONSTER</strong> / <strong>MONSTER9</strong>
        </p>
    </form>
</div>

</body>
</html>
