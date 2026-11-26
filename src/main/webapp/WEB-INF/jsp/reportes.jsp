<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Reportes" activo="reportes" subtitulo="Generados con JasperReports directamente desde SQL Server">
  <div class="reports">
    <form class="report" method="get" action="${ctx}/reportes" target="_blank"><input type="hidden" name="tipo" value="boletos">
      <h3>Boletos vendidos</h3><p>Detalle por boleto con cliente, ruta, asiento y total del período.</p>
      <div class="row2"><div><label>Desde</label><input type="date" name="fechaIni" value="${inicioMes}"></div><div><label>Hasta</label><input type="date" name="fechaFin" value="${hoy}"></div></div>
      <div class="actions" style="margin-top:14px;justify-content:flex-start"><button class="btn primary" name="formato" value="pdf">Ver PDF</button><button class="btn" name="formato" value="csv">Excel (CSV)</button></div></form>

    <form class="report" method="get" action="${ctx}/reportes" target="_blank"><input type="hidden" name="tipo" value="ventas_ruta">
      <h3>Ventas por ruta</h3><p>Boletos e ingresos agrupados por ruta con total general.</p>
      <div class="row2"><div><label>Desde</label><input type="date" name="fechaIni" value="${inicioMes}"></div><div><label>Hasta</label><input type="date" name="fechaFin" value="${hoy}"></div></div>
      <div class="actions" style="margin-top:14px;justify-content:flex-start"><button class="btn primary" name="formato" value="pdf">Ver PDF</button><button class="btn" name="formato" value="csv">Excel (CSV)</button></div></form>

    <form class="report" method="get" action="${ctx}/reportes" target="_blank"><input type="hidden" name="tipo" value="mantenimiento">
      <h3>Mantenimiento de buses</h3><p>Historial de mantenimientos y costo acumulado de la flota.</p>
      <div class="actions" style="margin-top:56px;justify-content:flex-start"><button class="btn primary" name="formato" value="pdf">Ver PDF</button><button class="btn" name="formato" value="csv">Excel (CSV)</button></div></form>

    <div class="report"><h3>Boleto individual</h3><p>Se imprime desde <b>Boletos → Imprimir</b> o con el enlace que aparece después de cada venta.</p>
      <div class="actions" style="margin-top:56px;justify-content:flex-start"><a class="btn" href="${ctx}/boletos">Ir a Boletos</a></div></div>
  </div>
</t:layout>
