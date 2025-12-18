<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>ElectroQuito · Editar Producto</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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

        .eq-main {
            flex: 1;
            display: flex;
            justify-content: center;
            padding: 8px 16px 40px;
        }

        .eq-main-inner {
            width: 100%;
            max-width: 560px;
        }

        .eq-card {
            margin-top: 8px;
            background: radial-gradient(circle at top left, #fff7ed, #fffbeb);
            border-radius: 32px;
            padding: 22px 22px 26px;
            box-shadow: 0 24px 55px rgba(15, 23, 42, 0.45);
        }

        .eq-card-title {
            margin: 0 0 6px;
            font-size: 24px;
            font-weight: 700;
        }

        .eq-card-subtitle {
            margin: 0 0 20px;
            font-size: 0.95rem;
            color: var(--eq-text-muted);
        }

        .eq-error {
            margin-bottom: 14px;
            font-size: 13px;
            color: #b91c1c;
            background: #fee2e2;
            border-radius: 16px;
            padding: 10px 12px;
        }

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
            font-size: 16px;
            color: #f97316;
        }

        .eq-input {
            flex: 1;
            border: none;
            font-size: 15px;
            background: transparent;
            outline: none;
            color: var(--eq-text-main);
        }

        .eq-upload-box {
            width: 100%;
            display: flex;
            flex-direction: column;
            gap: 6px;
            border: 1px dashed var(--eq-input-border);
            background: rgba(255, 255, 255, 0.8);
            border-radius: 18px;
            padding: 14px;
            color: var(--eq-text-muted);
            align-items: center;
        }

        .eq-upload-input {
            width: 100%;
            border: none;
            background: transparent;
        }

        .eq-image-preview {
            margin-top: 8px;
            border-radius: 18px;
            overflow: hidden;
            border: 1px solid rgba(0, 0, 0, 0.08);
        }

        .eq-image-preview img {
            width: 100%;
            display: block;
        }

        .eq-hint {
            font-size: 0.85rem;
            color: var(--eq-text-muted);
            margin: 6px 0 0;
        }

        .eq-actions {
            margin-top: 20px;
            display: flex;
            gap: 12px;
        }

        .eq-submit-btn {
            border: none;
            flex: 1;
            height: 52px;
            border-radius: 18px;
            font-size: 15px;
            font-weight: 700;
            background: #f97316;
            color: #111827;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            cursor: pointer;
            box-shadow: 0 18px 40px rgba(248, 113, 22, 0.85);
        }

        .eq-cancel-btn {
            border: none;
            border-radius: 18px;
            height: 52px;
            padding: 0 22px;
            font-weight: 600;
            background: rgba(15, 23, 42, 0.08);
            color: #1f2937;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }

        @media (max-width: 640px) {
            .eq-topbar {
                padding-inline: 16px;
            }
            .eq-card {
                border-radius: 28px;
            }
            .eq-actions {
                flex-direction: column;
            }
            .eq-cancel-btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<div class="eq-app-shell">

    <header class="eq-topbar">
        <a href="${pageContext.request.contextPath}/electroquito/productos"
           class="eq-back-btn" title="Atrás">
            &#8592;
        </a>
        <div class="eq-page-title">Editar Producto</div>
    </header>

    <main class="eq-main">
        <div class="eq-main-inner">
            <section class="eq-card">
                <h1 class="eq-card-title">
                    <c:choose>
                        <c:when test="${not empty producto and not empty producto.nombre}">
                            <c:out value="${producto.nombre}"/>
                        </c:when>
                        <c:otherwise>Datos del Producto</c:otherwise>
                    </c:choose>
                </h1>
                <p class="eq-card-subtitle">
                    Actualiza la información básica y cambia la imagen si lo necesitas.
                </p>

                <c:if test="${not empty error}">
                    <div class="eq-error">
                        <c:out value="${error}"/>
                    </div>
                </c:if>

                <form method="post"
                      action="${pageContext.request.contextPath}/electroquito/productos/editar"
                      enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${producto.id}"/>
                    <input type="hidden" name="imagenActual" value="${imagenActual}"/>

                    <div class="eq-field-group">
                        <div class="eq-field-label">Código *</div>
                        <div class="eq-input-box">
                            <span class="eq-input-icon">&#35;</span>
                            <input type="text"
                                   name="codigo"
                                   class="eq-input"
                                   value="${producto.codigo}"
                                   required>
                        </div>
                    </div>

                    <div class="eq-field-group">
                        <div class="eq-field-label">Nombre del Producto *</div>
                        <div class="eq-input-box">
                            <span class="eq-input-icon">&#128269;</span>
                            <input type="text"
                                   name="nombre"
                                   class="eq-input"
                                   value="${producto.nombre}"
                                   required>
                        </div>
                    </div>

                    <div class="eq-field-group">
                        <div class="eq-field-label">Precio de Venta *</div>
                        <div class="eq-input-box">
                            <span class="eq-input-icon">$</span>
                            <input type="number"
                                   step="0.01"
                                   min="0"
                                   name="precio"
                                   class="eq-input"
                                   value="${producto.precioVenta}"
                                   required>
                        </div>
                    </div>

                    <div class="eq-field-group">
                        <div class="eq-field-label">Imagen del Producto</div>
                        <label class="eq-upload-box">
                            <span>Selecciona una nueva imagen (PNG o JPG)</span>
                            <input type="file"
                                   name="imagen"
                                   accept="image/*"
                                   class="eq-upload-input">
                        </label>
                        <c:if test="${not empty imagenActual}">
                            <div class="eq-hint">Actualmente:</div>
                            <div class="eq-image-preview">
                                <img src="${imagenActual}"
                                     alt="${producto.nombre}">
                            </div>
                            <p class="eq-hint">
                                Si no subes un archivo se mantendrá la imagen actual.
                            </p>
                        </c:if>
                    </div>

                    <div class="eq-actions">
                        <button type="submit" class="eq-submit-btn">
                            &#128190; Guardar Cambios
                        </button>
                        <a class="eq-cancel-btn"
                           href="${pageContext.request.contextPath}/electroquito/productos">
                            Cancelar
                        </a>
                    </div>
                </form>
            </section>
        </div>
    </main>
</div>
</body>
</html>
