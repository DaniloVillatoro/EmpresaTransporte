<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Conductores" activo="conductores" subtitulo="Control de licencias y disponibilidad">
  <div class="grid even">
    <section class="panel"><h2>Registrar conductor</h2>
      <form method="post" action="${ctx}/conductores"><input type="hidden" name="_csrf" value="${csrf}">
        <div class="row2"><div><label>Nombre</label><input name="nombre" required maxlength="60"></div><div><label>Apellido</label><input name="apellido" required maxlength="60"></div></div>
        <div class="row2"><div><label>Identidad</label><input name="identidad" required maxlength="20"></div><div><label>Teléfono</label><input name="telefono" maxlength="20"></div></div>
        <label>Dirección</label><input name="direccion" maxlength="200">
        <div class="row3"><div><label>Licencia</label><input name="licencia" required maxlength="30"></div>
          <div><label>Vence</label><input type="date" name="fechaVencimientoLicencia" required></div>
          <div><label>Contratado</label><input type="date" name="fechaContratacion"></div></div>
        <button class="btn primary block">Guardar conductor</button></form></section>
    <section class="panel"><div class="toolbar"><h2 style="margin:0">Conductores</h2><input type="search" placeholder="Buscar…" data-filtro="#tb"></div>
      <div class="table-wrap"><table id="tb"><thead><tr><th>Nombre</th><th>Identidad</th><th>Licencia</th><th>Vence</th><th>Estado</th><th></th></tr></thead><tbody>
        <c:forEach items="${conductores}" var="c"><tr><td><c:out value="${c.nombre} ${c.apellido}"/></td><td><c:out value="${c.identidad}"/></td><td><c:out value="${c.licencia}"/></td>
          <td><c:out value="${c.fechaVencimientoLicencia}"/> <c:if test="${c.fechaVencimientoLicencia lt hoy}"><span class="badge b-red">Vencida</span></c:if></td>
          <td><t:badge estado="${c.estado}"/></td>
          <td><div class="actions"><form class="inline" method="post" action="${ctx}/conductores"><input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="accion" value="estado"><input type="hidden" name="id" value="${c.idConductor}">
                <select name="estado" onchange="if(this.value)this.form.submit()" style="width:auto;padding:5px 8px;font-size:13px"><option value="">Cambiar…</option><option value="Activo">Activo</option><option value="Inactivo">Inactivo</option></select></form></div></td></tr></c:forEach>
        <c:if test="${empty conductores}"><tr><td colspan="6" class="empty">Sin conductores.</td></tr></c:if></tbody></table></div></section>
  </div>
</t:layout>
