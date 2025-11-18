<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>ElectroQuito - Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap"
          rel="stylesheet">
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(180deg, #f97316 0%, #ffa228 45%, #ffe3b3 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: #111827;
        }
        .login-card {
            width: min(440px, 100%);
            background: #fff;
            border-radius: 32px;
            padding: 36px 32px 34px;
            box-shadow: 0 32px 70px rgba(134, 65, 4, 0.35);
        }
        .brand-hero {
            text-align: center;
        }
        .brand-photo {
            width: 180px;
            height: 180px;
            border-radius: 50%;
            margin: 0 auto 18px;
            background: #fff4e5;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }
        .brand-photo img { width: 100%; height: 100%; object-fit: cover; }
        .brand-title {
            margin: 0;
            font-size: 2rem;
            font-weight: 700;
            color: #bf360c;
        }
        .brand-subtitle {
            margin: 4px 0 20px;
            font-size: 0.95rem;
            color: #6b4b35;
        }
        .field-label {
            font-size: 0.85rem;
            font-weight: 600;
            color: #6b7280;
            margin-bottom: 6px;
        }
        .text-input {
            width: 100%;
            border-radius: 18px;
            border: 1px solid #d9d6f2;
            padding: 12px 16px;
            font-size: 1rem;
            font-family: inherit;
            margin-bottom: 16px;
            outline: none;
            transition: border-color 0.15s, box-shadow 0.15s;
        }
        .text-input:focus {
            border-color: #f97316;
            box-shadow: 0 0 0 3px rgba(249, 115, 22, 0.2);
        }
        .btn-login {
            width: 100%;
            border: none;
            border-radius: 999px;
            padding: 14px;
            background: linear-gradient(90deg, #2563eb, #1d4ed8);
            color: #fff;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            box-shadow: 0 18px 28px rgba(29, 78, 216, 0.35);
        }
        .alert-error {
            background: #fef2f2;
            color: #b91c1c;
            padding: 12px 14px;
            border-radius: 16px;
            font-size: 0.9rem;
            margin-bottom: 16px;
        }
        .helper-text {
            margin-top: 14px;
            text-align: center;
            font-size: 0.85rem;
            color: #a16207;
        }
    </style>
</head>
<body>
<div class="login-card">
    <div class="brand-hero">
        <div class="brand-photo">
            <img src="<c:url value='/assets/img/electroquito.jpg'/>" alt="ElectroQuito">
        </div>
        <p class="brand-title">ElectroQuito</p>
        <p class="brand-subtitle">Comercializadora de electrodomésticos</p>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/electroquito/login">
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <label class="field-label" for="username">Usuario</label>
        <input type="text"
               id="username"
               name="username"
               class="text-input"
               value="${username}"
               placeholder="Ej: MONSTER">

        <label class="field-label" for="password">Contraseña</label>
        <input type="password"
               id="password"
               name="password"
               class="text-input"
               placeholder="••••••••">

        <button type="submit" class="btn-login">Ingresar</button>
        <p class="helper-text">Usuario demo: <strong>MONSTER</strong> / <strong>MONSTER9</strong></p>
    </form>
</div>
</body>
</html>
