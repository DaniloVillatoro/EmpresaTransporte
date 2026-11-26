<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Pagos" activo="pagos" subtitulo="Se generan al vender o anular un boleto">
  <section class="panel"><div class="toolbar"><h2 style="margin:0">Historial de pagos</h2><input type="search" placeholder="Buscar…" data-filtro="#tb"></div>
    <div class="table-wrap"><table id="tb"><thead><tr><th>Boleto</th><th>Método</th><th class="num">Monto</th><th>Fecha</th><th>Referencia</th><th>Estado</th></tr></thead><tbody>
      <c:forEach items="${pagos}" var="p"><tr><td class="mono"><c:out value="${p.boleto}"/></td><td><c:out value="${p.metodo}"/></td>
        <td class="num">L <fmt:formatNumber value="${p.monto}" pattern="#,##0.00"/></td><td><c:out value="${p.fecha}"/></td><td class="mono"><c:out value="${p.referencia}"/></td><td><t:badge estado="${p.estado}"/></td></tr></c:forEach>
      <c:if test="${empty pagos}"><tr><td colspan="6" class="empty">Sin pagos.</td></tr></c:if></tbody></table></div></section>
</t:layout>
