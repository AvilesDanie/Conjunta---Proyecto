<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>BanQuito - Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap"
          rel="stylesheet">
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', sans-serif;
            background: radial-gradient(circle at top, #4c93ff, #0f62d8 45%, #082057 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: #0f172a;
        }
        .login-shell {
            width: min(420px, 100%);
            padding: 18px 18px 40px;
            display: flex;
            flex-direction: column;
            gap: 18px;
        }
        .login-back {
            color: #e0edff;
            text-decoration: none;
            font-size: 0.9rem;
            display: inline-flex;
            align-items: center;
            gap: 6px;
        }
        .login-hero { text-align: center; }
        .hero-photo {
            width: 180px;
            height: 180px;
            border-radius: 50%;
            margin: 0 auto 18px;
            background: #fff;
            box-shadow: 0 18px 40px rgba(10, 15, 34, 0.35);
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }
        .hero-photo img { width: 100%; height: 100%; object-fit: cover; }
        .hero-title {
            margin: 0;
            font-size: 2rem;
            font-weight: 700;
            color: #ffffff;
        }
        .hero-subtitle {
            margin: 4px 0 0;
            font-size: 0.95rem;
            color: rgba(255,255,255,0.85);
        }
        .login-card {
            margin-top: 10px;
            padding: 28px 26px 32px;
            border-radius: 28px;
            background: #ffffff;
            box-shadow: 0 30px 60px rgba(8, 15, 40, 0.35);
        }
        .form-label {
            font-size: 0.9rem;
            font-weight: 600;
            color: #4b5563;
            margin-bottom: 6px;
            display: block;
        }
        .text-input {
            width: 100%;
            border-radius: 16px;
            border: 1px solid #cbd5f5;
            padding: 12px 14px;
            font-size: 1rem;
            font-family: inherit;
            margin-bottom: 16px;
            outline: none;
            transition: box-shadow 0.15s, border-color 0.15s;
        }
        .text-input:focus {
            border-color: #0f62d8;
            box-shadow: 0 0 0 3px rgba(15, 98, 216, 0.2);
        }
        .btn-login {
            width: 100%;
            border: none;
            border-radius: 18px;
            padding: 14px;
            background: linear-gradient(90deg, #1151c9, #1f78ff);
            color: #fff;
            font-weight: 600;
            font-size: 1rem;
            cursor: pointer;
            box-shadow: 0 18px 30px rgba(17, 81, 201, 0.35);
        }
        .alert-error {
            background: #fee2e2;
            color: #b91c1c;
            padding: 12px 14px;
            border-radius: 16px;
            font-size: 0.9rem;
            margin-bottom: 16px;
        }
    </style>
</head>
<body>
<div class="login-shell">
    <a href="${pageContext.request.contextPath}/index.jsp" class="login-back">
        <span>&larr;</span>
        Atrás
    </a>

    <div class="login-hero">
        <div class="hero-photo">
            <img src="<c:url value='/assets/img/banquito.jpg'/>" alt="BanQuito">
        </div>
        <p class="hero-title">BanQuito</p>
        <p class="hero-subtitle">Sistema Bancario</p>
    </div>

    <div class="login-card">
        <%
            String error = (String) request.getAttribute("error");
            String usernameValor = (String) request.getAttribute("username");
            if (error != null) {
        %>
        <div class="alert-error"><%= error %></div>
        <% } %>

        <form action="${pageContext.request.contextPath}/banquito/login" method="post">
            <label class="form-label" for="username">Usuario</label>
            <input class="text-input"
                   type="text"
                   id="username"
                   name="username"
                   value="<%= usernameValor != null ? usernameValor : "" %>"
                   placeholder="Ej: MONSTER"
                   required>

            <label class="form-label" for="password">Contraseña</label>
            <input class="text-input"
                   type="password"
                   id="password"
                   name="password"
                   placeholder="••••••••"
                   required>

            <button type="submit" class="btn-login">INGRESAR</button>
        </form>
    </div>
</div>
</body>
</html>
