<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tabla de Amortizaci&oacute;n - BanQuito</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-gradient">
<fmt:setLocale value="es_EC"/>
<div class="app-container cuotas-page">

    <a href="${pageContext.request.contextPath}/banquito/creditos" class="back-link">
        <span>&larr;</span> Atr&aacute;s
    </a>

    <header class="page-header">
        <h1 class="page-title">Tabla de Amortizaci&oacute;n</h1>
    </header>

    <c:if test="${not empty error}">
        <div class="alert-error">
            <c:out value="${error}"/>
        </div>
    </c:if>

    <section class="form-card amortizacion-search-card">
        <h2 class="amortizacion-subtitle">Consultar cuotas de un cr&eacute;dito</h2>
        <p class="amortizacion-text">
            Ingrese el ID de un cr&eacute;dito y pulse <strong>Consultar</strong> para ver la tabla de amortizaci&oacute;n.
        </p>

        <form action="${pageContext.request.contextPath}/banquito/creditos/cuotas"
              method="get"
              class="form-vertical">
            <div class="form-group form-group-lined">
                <label for="idCredito" class="form-label">ID del cr&eacute;dito</label>
                <div class="input-wrapper">
                    <input
                        type="number"
                        min="1"
                        id="idCredito"
                        name="idCredito"
                        class="form-control form-control-light"
                        placeholder="Ej: 10"
                        value="${idCredito}"
                        required
                    >
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-primary btn-primary-lg btn-full">
                    Consultar
                </button>
            </div>
        </form>
    </section>

    <c:if test="${empty cuotas and not empty idCredito and empty error}">
        <div class="alert-info">
            No encontramos cuotas para el cr&eacute;dito ingresado. Verifique el identificador e intente nuevamente.
        </div>
    </c:if>

    <c:if test="${not empty cuotas}">
        <section class="cuotas-result-section">
            <article class="cuota-summary-card">
                <h2 class="cuota-summary-title">
                    Cr&eacute;dito #<c:out value="${idCreditoResumen}"/>
                </h2>
                <p class="cuota-summary-line">
                    <span>Total de cuotas:</span>
                    <strong>
                        <c:out value="${not empty totalCuotas ? totalCuotas : fn:length(cuotas)}"/>
                    </strong>
                </p>
                <p class="cuota-summary-line">
                    <span>Cuota mensual:</span>
                    <strong>
                        <fmt:formatNumber value="${cuotaMensual}"
                                          type="currency"
                                          currencySymbol="$"
                                          minFractionDigits="2"
                                          maxFractionDigits="2"/>
                    </strong>
                </p>
            </article>

            <div class="cuotas-list">
                <c:forEach var="cuota" items="${cuotas}">
                    <c:set var="estadoActual"
                           value="${empty cuota.estado ? 'PENDIENTE' : cuota.estado}"/>
                    <c:set var="estadoClase"
                           value="${estadoActual eq 'PAGADA' ? 'status-pill-pagada' :
                                   (estadoActual eq 'VENCIDA' ? 'status-pill-vencida' : 'status-pill-pendiente')}"/>
                    <c:set var="estadoTexto"
                           value="${estadoActual eq 'PAGADA' ? 'Pagada' :
                                   (estadoActual eq 'PENDIENTE' ? 'Pendiente' : estadoActual)}"/>

                    <article class="cuota-card">
                        <div class="cuota-card-header">
                            <h3 class="cuota-card-title">
                                Cuota #<c:out value="${cuota.numeroCuota}"/>
                            </h3>
                            <span class="status-pill ${estadoClase}">
                                <c:out value="${estadoTexto}"/>
                            </span>
                        </div>
                        <div class="cuota-card-body">
                            <p>
                                <span>Valor cuota:</span>
                                <strong>
                                    <fmt:formatNumber value="${cuota.valorCuota}"
                                                      type="currency"
                                                      currencySymbol="$"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"/>
                                </strong>
                            </p>
                            <p>
                                <span>Inter&eacute;s:</span>
                                <strong>
                                    <fmt:formatNumber value="${cuota.interes}"
                                                      type="currency"
                                                      currencySymbol="$"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"/>
                                </strong>
                            </p>
                            <p>
                                <span>Capital:</span>
                                <strong>
                                    <fmt:formatNumber value="${cuota.capital}"
                                                      type="currency"
                                                      currencySymbol="$"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"/>
                                </strong>
                            </p>
                            <p>
                                <span>Saldo pendiente:</span>
                                <strong class="saldo-pendiente">
                                    <fmt:formatNumber value="${cuota.saldoPendiente}"
                                                      type="currency"
                                                      currencySymbol="$"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"/>
                                </strong>
                            </p>
                        </div>
                    </article>
                </c:forEach>
            </div>
        </section>
    </c:if>

</div>
</body>
</html>
