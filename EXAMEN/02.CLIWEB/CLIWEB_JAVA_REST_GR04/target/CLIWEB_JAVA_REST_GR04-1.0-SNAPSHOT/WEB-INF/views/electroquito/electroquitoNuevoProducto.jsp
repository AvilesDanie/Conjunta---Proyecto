<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>ElectroQuito – Nuevo Producto</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Fuente tipo móvil -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap"
          rel="stylesheet">

    <style>
        :root {
            --eq-orange: #f97316;
            --eq-orange-dark: #ea580c;
            --eq-bg-card: #fff7ed;
            --eq-input-border: #e5e7eb;
            --eq-text-main: #111827;
            --eq-text-muted: #9ca3af;
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: "Poppins", system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background: linear-gradient(to bottom, #fb923c 0%, #f97316 40%, #f97316 100%);
            min-height: 100vh;
            color: var(--eq-text-main);
        }

        .eq-app-shell {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        /* Top bar similar al móvil */
        .eq-topbar {
            display: flex;
            align-items: center;
            gap: 14px;
            padding: 16px 24px 12px;
            color: #ffffff;
        }

        .eq-back-btn {
            width: 34px;
            height: 34px;
            border-radius: 999px;
            border: 1px solid rgba(255, 255, 255, 0.35);
            display: inline-flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
            color: #ffffff;
            font-size: 18px;
            backdrop-filter: blur(4px);
        }

        .eq-page-title {
            font-size: 24px;
            font-weight: 700;
        }

        /* Contenido principal */
        .eq-main {
            flex: 1;
            display: flex;
            justify-content: center;
            padding: 8px 16px 40px;
        }

        .eq-main-inner {
            width: 100%;
            max-width: 520px;
        }

        .eq-card {
            margin-top: 8px;
            background: radial-gradient(circle at top left, #fff7ed, #fffbeb);
            border-radius: 32px;
            padding: 22px 22px 26px;
            box-shadow: 0 24px 55px rgba(15, 23, 42, 0.45);
        }

        .eq-card-title {
            margin: 0 0 18px;
            font-size: 22px;
            font-weight: 700;
        }

        /* Mensaje de error */
        .eq-error {
            margin-bottom: 14px;
            font-size: 13px;
            color: #b91c1c;
            background: #fee2e2;
            border-radius: 16px;
            padding: 10px 12px;
        }

        /* Inputs tipo tarjeta */
        .eq-field-group {
            margin-bottom: 14px;
        }

        .eq-field-label {
            font-size: 12px;
            font-weight: 500;
            color: var(--eq-text-muted);
            margin-bottom: 4px;
        }

        .eq-input-box {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 12px 14px;
            border-radius: 18px;
            border: 1px solid var(--eq-input-border);
            background: #ffffff;
        }

        .eq-input-icon {
            font-size: 18px;
            color: #cbd5f5;
            flex-shrink: 0;
        }

        .eq-input {
            border: none;
            outline: none;
            width: 100%;
            font-size: 14px;
            color: var(--eq-text-main);
        }

        .eq-input::placeholder {
            color: #d1d5db;
        }

        .eq-upload-box {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
            padding: 12px 14px;
            border-radius: 18px;
            border: 1px dashed #f97316;
            background: rgba(255,255,255,0.7);
        }

        .eq-upload-box span {
            font-size: 13px;
            color: var(--eq-text-muted);
        }

        .eq-upload-input {
            flex: 0 0 auto;
            border: none;
            font-size: 13px;
        }

        /* Botón guardar */
        .eq-actions {
            margin-top: 16px;
        }

        .eq-submit-btn {
            width: 100%;
            border: none;
            border-radius: 999px;
            padding: 13px 18px;
            font-size: 15px;
            font-weight: 700;
            letter-spacing: 0.03em;
            text-transform: uppercase;
            background: #f97316;
            color: #111827;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            cursor: pointer;
            box-shadow: 0 18px 40px rgba(248, 113, 22, 0.85);
        }

        .eq-submit-icon {
            font-size: 18px;
        }

        @media (max-width: 640px) {
            .eq-topbar {
                padding-inline: 16px;
            }
            .eq-card {
                border-radius: 28px;
            }
        }
    </style>
</head>
<body>
<div class="eq-app-shell">

    <!-- Top bar -->
    <header class="eq-topbar">
        <a href="${pageContext.request.contextPath}/electroquito/productos"
           class="eq-back-btn" title="Atrás">
            ←
        </a>
        <div class="eq-page-title">Nuevo Producto</div>
    </header>

    <!-- Contenido -->
    <main class="eq-main">
        <div class="eq-main-inner">
            <section class="eq-card">
                <h1 class="eq-card-title">Datos del Producto</h1>

                <!-- Mensaje de error -->
                <c:if test="${not empty error}">
                    <div class="eq-error">
                        <c:out value="${error}"/>
                    </div>
                </c:if>

                <form method="post"
                      action="${pageContext.request.contextPath}/electroquito/productos/nuevo"
                      enctype="multipart/form-data">

                    <!-- Código -->
                    <div class="eq-field-group">
                        <div class="eq-field-label">Código *</div>
                        <div class="eq-input-box">
                            <span class="eq-input-icon">▦</span>
                            <input type="text"
                                   name="codigo"
                                   class="eq-input"
                                   placeholder="Ej: REF-001"
                                   value="${codigo != null ? codigo : ''}"
                                   required>
                        </div>
                    </div>

                    <!-- Nombre -->
                    <div class="eq-field-group">
                        <div class="eq-field-label">Nombre del Producto *</div>
                        <div class="eq-input-box">
                            <span class="eq-input-icon">🖥</span>
                            <input type="text"
                                   name="nombre"
                                   class="eq-input"
                                   placeholder="Ej: Refrigeradora Samsung 18 pies"
                                   value="${nombre != null ? nombre : ''}"
                                   required>
                        </div>
                    </div>

                    <!-- Precio -->
                    <div class="eq-field-group">
                        <div class="eq-field-label">Precio de Venta *</div>
                        <div class="eq-input-box">
                            <span class="eq-input-icon">$</span>
                            <input type="number"
                                   step="0.01"
                                   min="0"
                                   name="precio"
                                   class="eq-input"
                                   placeholder="Ej: 1250.00"
                                   value="${precio != null ? precio : ''}"
                                   required>
                        </div>
                    </div>

                    <!-- Imagen -->
                    <div class="eq-field-group">
                        <div class="eq-field-label">Imagen del Producto *</div>
                        <label class="eq-upload-box">
                            <span>Selecciona una imagen (PNG o JPG)</span>
                            <input type="file"
                                   name="imagen"
                                   accept="image/*"
                                   class="eq-upload-input"
                                   required>
                        </label>
                    </div>

                    <!-- Botón -->
                    <div class="eq-actions">
                        <button type="submit" class="eq-submit-btn">
                            <span class="eq-submit-icon">💾</span>
                            GUARDAR PRODUCTO
                        </button>
                    </div>
                </form>
            </section>
        </div>
    </main>
</div>
</body>
</html>
