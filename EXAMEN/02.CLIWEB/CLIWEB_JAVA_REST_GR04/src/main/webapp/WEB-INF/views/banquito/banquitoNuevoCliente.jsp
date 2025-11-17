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
</head>
<body class="bg-gradient">
<div class="app-container">

    <!-- HEADER, igual estilo que el resto -->
    <header class="app-header">
        <a href="<%=request.getContextPath()%>/banquito/home" class="back-link">
            <span>&larr;</span>
        </a>
        <div class="header-titles">
            <div class="header-title">Registrar Cliente</div>
            <div class="header-subtitle">
                Ingrese los datos del nuevo cliente BanQuito.
            </div>
        </div>
    </header>

    <!-- CARD DE INFO (similar a la de la app móvil) -->
    <section class="page-card page-card--top">
        <div class="section-title">Información del Cliente</div>
        <p class="section-subtitle">
            Complete el formulario para registrar un nuevo cliente y crear
            su cuenta inicial en el banco.
        </p>
    </section>

    <!-- FORMULARIO PRINCIPAL -->
    <section class="page-card page-card--form">

        <% if (error != null) { %>
            <div class="alert-error"><%= error %></div>
        <% } %>

        <% if (exito != null) { %>
            <div class="alert-success"><%= exito %></div>
        <% } %>

        <form method="post"
              action="<%=request.getContextPath()%>/banquito/clientes/nuevo">

            <!-- Nombre -->
            <div class="form-group">
                <label class="form-label" for="nombre">
                    Nombres y Apellidos *
                </label>
                <input class="form-control"
                       type="text"
                       id="nombre"
                       name="nombre"
                       placeholder="Ej. Carlos Alberto Mendoza Torres"
                       value="<%= nombre != null ? nombre : "" %>"
                       required>
            </div>

            <!-- Cédula -->
            <div class="form-group">
                <label class="form-label" for="cedula">
                    Cédula *
                </label>
                <input class="form-control"
                       type="text"
                       id="cedula"
                       name="cedula"
                       placeholder="Ej. 0987654321"
                       value="<%= cedula != null ? cedula : "" %>"
                       required>
            </div>

            <!-- Fecha de nacimiento -->
            <div class="form-group">
                <label class="form-label" for="fechaNacimiento">
                    Fecha de Nacimiento *
                </label>
                <input class="form-control"
                       type="date"
                       id="fechaNacimiento"
                       name="fechaNacimiento"
                       value="<%= fechaNacimiento != null ? fechaNacimiento : "" %>"
                       required>
            </div>

            <!-- Estado civil -->
            <div class="form-group">
                <label class="form-label" for="estadoCivil">
                    Estado Civil *
                </label>
                <select class="form-control"
                        id="estadoCivil"
                        name="estadoCivil"
                        required>
                    <option value="">Seleccione...</option>
                    <option value="SOLTERO"
                        <%= "SOLTERO".equals(estadoCivil) ? "selected" : "" %>>
                        Soltero
                    </option>
                    <option value="CASADO"
                        <%= "CASADO".equals(estadoCivil) ? "selected" : "" %>>
                        Casado
                    </option>
                    <option value="DIVORCIADO"
                        <%= "DIVORCIADO".equals(estadoCivil) ? "selected" : "" %>>
                        Divorciado
                    </option>
                    <option value="VIUDO"
                        <%= "VIUDO".equals(estadoCivil) ? "selected" : "" %>>
                        Viudo
                    </option>
                </select>
            </div>

            <!-- Tipo de cuenta inicial -->
            <div class="form-group">
                <label class="form-label" for="tipoCuentaInicial">
                    Tipo de Cuenta Inicial *
                </label>
                <select class="form-control"
                        id="tipoCuentaInicial"
                        name="tipoCuentaInicial"
                        required>
                    <option value="">Seleccione...</option>
                    <option value="AHORROS"
                        <%= "AHORROS".equals(tipoCuentaInicial) ? "selected" : "" %>>
                        Ahorros
                    </option>
                    <option value="CORRIENTE"
                        <%= "CORRIENTE".equals(tipoCuentaInicial) ? "selected" : "" %>>
                        Corriente
                    </option>
                </select>
            </div>

            <!-- Botón grande tipo app -->
            <div class="form-actions">
                <button type="submit" class="btn-primary btn-full">
                    Registrar Cliente
                </button>
            </div>
        </form>
    </section>
</div>
</body>
</html>
