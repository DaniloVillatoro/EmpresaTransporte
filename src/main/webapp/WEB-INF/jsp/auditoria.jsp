<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Auditoría" activo="auditoria" subtitulo="Bitácora de cambios registrada por la base de datos">
  <section class="panel"><div class="toolbar"><h2 style="margin:0">Últimos movimientos</h2><input type="search" placeholder="Filtrar…" data-filtro="#tb"></div>
    <div class="table-wrap"><table id="tb"><thead><tr><th>Fecha</th><th>Tabla</th><th>Acción</th><th>Antes</th><th>Después</th><th>Usuario</th><th>Equipo</th></tr></thead><tbody>
      <c:forEach items="${registros}" var="a"><tr><td><c:out value="${fn:replace(fn:substring(a.fechaCambio, 0, 19), 'T', ' ')}"/></td>
        <td><c:out value="${a.tablaAfectada}"/> #${a.idRegistro}</td><td><span class="badge b-gray"><c:out value="${a.accion}"/></span></td>
        <td><c:out value="${a.valorAnterior}"/></td><td><c:out value="${a.valorNuevo}"/></td><td><c:out value="${a.usuario}"/></td><td><c:out value="${a.equipo}"/></td></tr></c:forEach>
      <c:if test="${empty registros}"><tr><td colspan="7" class="empty">Sin movimientos.</td></tr></c:if></tbody></table></div></section>
</t:layout>
