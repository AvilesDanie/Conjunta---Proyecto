<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Créditos</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap"
          rel="stylesheet">
    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(180deg, #0f63d9 0%, #0b3b94 45%, #091d52 100%);
            display: flex;
            justify-content: center;
            padding: 32px 18px 40px;
        }

        .credit-shell {
            width: min(960px, 100%);
            color: #fff;
        }

        .credit-header {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 28px;
        }

        .back-link {
            color: #dce8ff;
            text-decoration: none;
            font-weight: 500;
        }

        .credit-title {
            margin: 0;
            font-size: 2.1rem;
            font-weight: 700;
        }

        .card-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 24px;
        }

        .credit-card {
            border-radius: 30px;
            padding: 26px 24px;
            background: #f8fbff;
            color: #1e293b;
            box-shadow: 0 28px 60px rgba(4, 12, 40, 0.4);
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        .credit-card h3 {
            margin: 0;
            font-size: 1.2rem;
            font-weight: 700;
        }

        .credit-card p {
            margin: 0;
            color: #4b5563;
            font-size: 0.95rem;
        }

        .credit-card button,
        .credit-card a {
            border: none;
            border-radius: 999px;
            padding: 12px 16px;
            font-size: 0.95rem;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            color: #fff;
        }

        .btn-evaluar {
            background: #1f51ff;
            box-shadow: 0 14px 30px rgba(31, 81, 255, 0.35);
        }

        .btn-cuotas {
            background: #15803d;
            box-shadow: 0 14px 30px rgba(21, 128, 61, 0.35);
        }

        .modal-overlay {
            position: fixed;
            inset: 0;
            background: rgba(4, 10, 32, 0.65);
            display: none;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .modal-overlay.is-visible {
            display: flex;
        }

        .modal-card {
            width: min(380px, 100%);
            border-radius: 24px;
            background: #ffffff;
            color: #0f172a;
            padding: 24px;
            box-shadow: 0 22px 50px rgba(0,0,0,0.45);
        }

        .modal-card h4 {
            margin: 0 0 12px;
            font-size: 1.2rem;
        }

        .modal-card input {
            width: 100%;
            border-radius: 16px;
            border: 1px solid #cfd8f3;
            padding: 12px 14px;
            font-size: 1rem;
            font-family: inherit;
            margin-bottom: 16px;
        }

        .modal-actions {
            display: flex;
            justify-content: flex-end;
            gap: 12px;
        }

        .btn-secondary {
            border: none;
            border-radius: 999px;
            padding: 10px 18px;
            background: #e5e7eb;
            color: #1e293b;
            cursor: pointer;
        }

        .btn-primary {
            border: none;
            border-radius: 999px;
            padding: 10px 18px;
            background: #2563eb;
            color: #fff;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="credit-shell">
    <div class="credit-header">
        <a class="back-link" href="${pageContext.request.contextPath}/banquito/home">← Atrás</a>
        <h1 class="credit-title">Gestión de Créditos</h1>
    </div>

    <div class="card-grid">
        <article class="credit-card">
            <h3>Evaluación de Créditos</h3>
            <p>Evalúe si un cliente es sujeto de crédito según su historial de movimientos.</p>
            <a class="btn-evaluar" href="${pageContext.request.contextPath}/banquito/creditos/evaluar">
                Evaluar Nuevo Crédito
            </a>
        </article>

        <article class="credit-card">
            <h3>Consultar Cuotas</h3>
            <p>Ingrese el ID de un crédito para ver su tabla de amortización.</p>
            <button type="button" class="btn-cuotas" id="openCuotasModal">
                Ver Cuotas de Crédito
            </button>
        </article>
    </div>
</div>

<div class="modal-overlay" id="cuotasModal">
    <div class="modal-card">
        <h4>Consultar Cuotas</h4>
        <p style="margin-top:0;margin-bottom:14px;color:#4b5563;font-size:0.95rem;">
            Ingrese el ID del crédito:
        </p>
        <input type="number" min="1" step="1" id="creditoIdInput" placeholder="ID Crédito">
        <div class="modal-actions">
            <button type="button" class="btn-secondary" id="closeCuotasModal">Cancelar</button>
            <button type="button" class="btn-primary" id="consultarCuotasBtn">Consultar</button>
        </div>
    </div>
</div>

<script>
    const modal = document.getElementById('cuotasModal');
    const openBtn = document.getElementById('openCuotasModal');
    const closeBtn = document.getElementById('closeCuotasModal');
    const consultarBtn = document.getElementById('consultarCuotasBtn');
    const input = document.getElementById('creditoIdInput');

    const toggleModal = (show) => {
        modal.classList.toggle('is-visible', show);
        if (show) {
            input.focus();
        } else {
            input.value = '';
        }
    };

    openBtn.addEventListener('click', () => toggleModal(true));
    closeBtn.addEventListener('click', () => toggleModal(false));
    modal.addEventListener('click', (ev) => {
        if (ev.target === modal) toggleModal(false);
    });

    consultarBtn.addEventListener('click', () => {
        const id = input.value.trim();
        if (!id) {
            input.focus();
            return;
        }
        const base = '${pageContext.request.contextPath}/banquito/creditos/cuotas?idCredito=';
        window.location.href = base + encodeURIComponent(id);
    });
</script>
</body>
</html>
