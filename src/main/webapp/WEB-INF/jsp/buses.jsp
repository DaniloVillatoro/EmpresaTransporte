<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Buses" activo="buses" subtitulo="Flota: al registrar un bus se crean sus asientos automáticamente">
  <div class="grid even">
    <section class="panel"><h2>Registrar bus</h2>
      <form method="post" action="${ctx}/buses"><input type="hidden" name="_csrf" value="${csrf}">
        <div class="row2"><div><label>Placa</label><input name="placa" placeholder="HN-204" required maxlength="15"></div>
          <div><label>Capacidad</label><input type="number" name="capacidad" min="1" max="80" required></div></div>
        <div class="row2"><div><label>Marca</label><input name="marca" maxlength="50"></div><div><label>Modelo</label><input name="modelo" maxlength="50"></div></div>
        <div class="row3"><div><label>Año</label><input type="number" name="anio" min="1900" max="2100"></div>
          <div><label>Tipo</label><input name="tipoBus" placeholder="Ejecutivo" maxlength="30"></div>
          <div><label>Adquirido</label><input type="date" name="fechaAdquisicion"></div></div>
        <button class="btn primary block">Guardar bus</button></form></section>
    <section class="panel"><div class="toolbar"><h2 style="margin:0">Flota</h2><input type="search" placeholder="Buscar…" data-filtro="#tb"></div>
      <div class="table-wrap"><table id="tb"><thead><tr><th>Placa</th><th>Unidad</th><th class="num">Cap.</th><th>Tipo</th><th>Estado</th><th></th></tr></thead><tbody>
        <c:forEach items="${buses}" var="b"><tr><td><b><c:out value="${b.placa}"/></b></td><td><c:out value="${b.marca} ${b.modelo} ${b.anio}"/></td>
          <td class="num">${b.capacidad}</td><td><c:out value="${b.tipoBus}"/></td><td><t:badge estado="${b.estado}"/></td>
          <td><div class="actions"><form class="inline" method="post" action="${ctx}/buses"><input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="accion" value="estado"><input type="hidden" name="id" value="${b.idBus}">
                <select name="estado" onchange="if(this.value)this.form.submit()" style="width:auto;padding:5px 8px;font-size:13px"><option value="">Cambiar…</option><option value="Disponible">Disponible</option><option value="En ruta">En ruta</option><option value="Mantenimiento">Mantenimiento</option><option value="Inactivo">Inactivo</option></select></form></div></td></tr></c:forEach>
        <c:if test="${empty buses}"><tr><td colspan="6" class="empty">Sin buses.</td></tr></c:if></tbody></table></div></section>
  </div>
</t:layout>
