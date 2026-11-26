<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Clientes" activo="clientes" subtitulo="Pasajeros registrados">
  <div class="grid even">
    <section class="panel"><h2>Registrar cliente</h2>
      <form method="post" action="${ctx}/clientes"><input type="hidden" name="_csrf" value="${csrf}">
        <div class="row2"><div><label>Nombre</label><input name="nombre" required maxlength="60"></div><div><label>Apellido</label><input name="apellido" required maxlength="60"></div></div>
        <div class="row2"><div><label>Identidad</label><input name="identidad" maxlength="20"></div><div><label>Teléfono</label><input name="telefono" maxlength="20"></div></div>
        <label>Correo</label><input type="email" name="correo" maxlength="100">
        <label>Dirección</label><input name="direccion" maxlength="200">
        <button class="btn primary block">Guardar cliente</button></form></section>
    <section class="panel"><div class="toolbar"><h2 style="margin:0">Clientes</h2><input type="search" placeholder="Buscar por nombre o identidad…" data-filtro="#tb"></div>
      <div class="table-wrap"><table id="tb"><thead><tr><th>Nombre</th><th>Identidad</th><th>Teléfono</th><th>Estado</th><th></th></tr></thead><tbody>
        <c:forEach items="${clientes}" var="c"><tr><td><c:out value="${c.nombre} ${c.apellido}"/></td><td><c:out value="${c.identidad}"/></td><td><c:out value="${c.telefono}"/></td>
          <td><t:badge estado="${c.estado}"/></td>
          <td><div class="actions"><form class="inline" method="post" action="${ctx}/clientes"><input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="accion" value="estado"><input type="hidden" name="id" value="${c.idCliente}">
                <select name="estado" onchange="if(this.value)this.form.submit()" style="width:auto;padding:5px 8px;font-size:13px"><option value="">Cambiar…</option><option value="Activo">Activo</option><option value="Inactivo">Inactivo</option></select></form></div></td></tr></c:forEach>
        <c:if test="${empty clientes}"><tr><td colspan="5" class="empty">Sin clientes.</td></tr></c:if></tbody></table></div></section>
  </div>
</t:layout>
