<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Panel principal" activo="panel">
  <div class="stats">
    <div class="stat"><span>Boletos hoy</span><strong>${ind.boletosHoy}</strong></div>
    <div class="stat"><span>Ventas hoy</span><strong>L <fmt:formatNumber value="${ind.ventasHoy}" pattern="#,##0"/></strong></div>
    <div class="stat"><span>Buses en ruta</span><strong>${ind.busesEnRuta}</strong></div>
    <div class="stat"><span>En mantenimiento</span><strong>${ind.enMantenimiento}</strong></div>
  </div>
 
  <div class="grid">
    <div>
      <section class="panel">
        <h2>Próximas salidas</h2>
        <div class="table-wrap"><table>
          <thead><tr><th>Ruta</th><th>Hora</th><th>Bus</th><th class="num">Libres</th><th>Estado</th></tr></thead>
          <tbody>
          <c:forEach items="${salidas}" var="s">
            <tr><td><c:out value="${s.ruta}"/></td><td><c:out value="${s.hora}"/></td><td><c:out value="${s.bus}"/></td>
                <td class="num">${s.libres}</td><td><t:badge estado="${s.estado}"/></td></tr>
          </c:forEach>
          <c:if test="${empty salidas}"><tr><td colspan="5" class="empty">No hay salidas programadas.</td></tr></c:if>
          </tbody></table></div>
      </section>

      <section class="panel">
        <h2>Ventas de los últimos 7 días <small>en lempiras</small></h2>
        <c:set var="max" value="1"/>
        <c:forEach items="${semana}" var="d"><c:if test="${d.total gt max}"><c:set var="max" value="${d.total}"/></c:if></c:forEach>
        <div class="bars">
          <c:forEach items="${semana}" var="d" varStatus="st">
            <fmt:formatNumber value="${d.total * 100 / max}" maxFractionDigits="0" groupingUsed="false" var="alto"/>
            <div class="bar ${st.last ? 'hoy' : ''}">
              <em><fmt:formatNumber value="${d.total}" pattern="#,##0"/></em>
              <b style="height:${alto}%"></b><span><c:out value="${d.dia}"/></span>
            </div>
          </c:forEach>
        </div>
      </section>
    </div>

    <section class="panel">
      <h2>Vender boleto</h2>
      <t:venta volver="/panel"/>
    </section>
  </div>
</t:layout>
