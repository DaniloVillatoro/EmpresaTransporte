<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Boletos" activo="boletos" subtitulo="Venta en taquilla, historial y anulaciones">
  <div class="grid">
    <section class="panel">
      <div class="toolbar"><h2 style="margin:0">Boletos emitidos</h2>
        <input type="search" placeholder="Buscar código, cliente, ruta…" data-filtro="#tb"></div>
      <div class="table-wrap"><table id="tb">
        <thead><tr><th>Código</th><th>Cliente</th><th>Ruta / salida</th><th class="num">Asiento</th><th class="num">Precio</th><th>Estado</th><th></th></tr></thead>
        <tbody>
        <c:forEach items="${boletos}" var="b">
          <tr>
            <td class="mono"><c:out value="${b.codigo}"/></td>
            <td><c:out value="${b.cliente}"/><br><small style="color:var(--muted)"><c:out value="${b.identidad}"/></small></td>
            <td><c:out value="${b.ruta}"/><br><small style="color:var(--muted)"><c:out value="${b.salida}"/></small></td>
            <td class="num">${b.asiento}</td>
            <td class="num">L <fmt:formatNumber value="${b.precio}" pattern="#,##0.00"/></td>
            <td><t:badge estado="${b.estado}"/></td>
            <td><div class="actions">
              <a class="btn sm" target="_blank" rel="noopener" href="${ctx}/reportes?tipo=boleto&id=${b.id}">Imprimir</a>
              <c:if test="${b.estado eq 'Pagado'}">
                <form class="inline" method="post" action="${ctx}/boletos" data-confirmar="¿Anular el boleto ${b.codigo}? Se reembolsará el pago.">
                  <input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="accion" value="anular">
                  <input type="hidden" name="idBoleto" value="${b.id}"><button class="btn sm danger">Anular</button></form>
              </c:if></div></td>
          </tr>
        </c:forEach>
        <c:if test="${empty boletos}"><tr><td colspan="7" class="empty">Aún no hay boletos.</td></tr></c:if>
        </tbody></table></div>
    </section>
    <section class="panel"><h2>Vender boleto</h2><t:venta volver="/boletos"/></section>
  </div>
</t:layout>
