<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Mantenimiento" activo="mantenimiento" subtitulo="Al abrir un mantenimiento el bus sale de servicio; al finalizar vuelve a Disponible">
  <div class="grid even">
    <section class="panel"><h2>Registrar mantenimiento</h2>
      <form method="post" action="${ctx}/mantenimiento"><input type="hidden" name="_csrf" value="${csrf}">
        <label>Bus</label><select name="idBus" required><option value="">Seleccionar…</option>
          <c:forEach items="${buses}" var="b"><option value="${b.idBus}"><c:out value="${b.placa} · ${b.marca}"/></option></c:forEach></select>
        <div class="row2"><div><label>Fecha</label><input type="date" name="fecha" required></div><div><label>Tipo</label><input name="tipo" placeholder="Preventivo / Correctivo" required maxlength="40"></div></div>
        <label>Descripción</label><textarea name="descripcion" rows="2" maxlength="300"></textarea>
        <div class="row2"><div><label>Kilometraje</label><input type="number" name="kilometraje" min="0"></div><div><label>Costo (L)</label><input type="number" name="costo" min="0" step="0.01"></div></div>
        <button class="btn primary block">Guardar</button></form></section>
    <section class="panel"><h2>Historial</h2>
      <div class="table-wrap"><table><thead><tr><th>Bus</th><th>Fecha</th><th>Tipo</th><th class="num">Costo</th><th>Estado</th><th></th></tr></thead><tbody>
        <c:forEach items="${mantenimientos}" var="m"><tr><td><b><c:out value="${m.bus}"/></b></td><td><c:out value="${m.fecha}"/></td>
          <td><c:out value="${m.tipo}"/><br><small style="color:var(--muted)"><c:out value="${m.descripcion}"/></small></td>
          <td class="num">L <fmt:formatNumber value="${m.costo}" pattern="#,##0.00"/></td><td><t:badge estado="${m.estado}"/></td>
          <td><div class="actions"><form class="inline" method="post" action="${ctx}/mantenimiento"><input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="accion" value="estado"><input type="hidden" name="id" value="${m.id}">
                <select name="estado" onchange="if(this.value)this.form.submit()" style="width:auto;padding:5px 8px;font-size:13px"><option value="">Cambiar…</option><option value="Pendiente">Pendiente</option><option value="En proceso">En proceso</option><option value="Finalizado">Finalizado</option></select></form></div></td></tr></c:forEach>
        <c:if test="${empty mantenimientos}"><tr><td colspan="6" class="empty">Sin registros.</td></tr></c:if></tbody></table></div></section>
  </div>
</t:layout>
