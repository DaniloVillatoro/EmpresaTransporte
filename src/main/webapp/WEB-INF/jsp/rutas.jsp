<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Rutas" activo="rutas" subtitulo="Rutas entre terminales">
  <div class="grid even">
    <div>
      <section class="panel"><h2>Nueva ruta</h2>
        <form method="post" action="${ctx}/rutas"><input type="hidden" name="_csrf" value="${csrf}">
          <label>Nombre</label><input name="nombreRuta" placeholder="Ej. SPS - TGU" required maxlength="100">
          <div class="row2">
            <div><label>Origen</label><select name="terminalOrigen" required><option value="">…</option><c:forEach items="${terminales}" var="x"><c:if test="${x.estado eq 'Activa'}"><option value="${x.idTerminal}"><c:out value="${x.ciudad}"/></option></c:if></c:forEach></select></div>
            <div><label>Destino</label><select name="terminalDestino" required><option value="">…</option><c:forEach items="${terminales}" var="x"><c:if test="${x.estado eq 'Activa'}"><option value="${x.idTerminal}"><c:out value="${x.ciudad}"/></option></c:if></c:forEach></select></div>
          </div>
          <div class="row2">
            <div><label>Distancia (km)</label><input type="number" name="distanciaKm" step="0.01" min="0.01" required></div>
            <div><label>Duración estimada</label><input type="time" name="duracionEstimada" required></div>
          </div>
          <button class="btn primary block">Guardar ruta</button></form>
      </section>
      <section class="panel"><h2>Nueva terminal</h2>
        <form method="post" action="${ctx}/rutas"><input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="accion" value="terminal">
          <div class="row2"><div><label>Nombre</label><input name="nombre" required maxlength="100"></div><div><label>Ciudad</label><input name="ciudad" required maxlength="80"></div></div>
          <label>Dirección</label><input name="direccion" maxlength="200">
          <div class="row2"><div><label>Teléfono</label><input name="telefono" maxlength="20"></div><div><label>Correo</label><input type="email" name="correo" maxlength="100"></div></div>
          <button class="btn block">Guardar terminal</button></form>
      </section>
    </div>
    <section class="panel"><h2>Rutas registradas</h2>
      <div class="table-wrap"><table><thead><tr><th>Ruta</th><th>Origen → Destino</th><th class="num">Km</th><th>Duración</th><th>Estado</th><th></th></tr></thead><tbody>
        <c:forEach items="${rutas}" var="r"><tr><td><c:out value="${r.nombre}"/></td><td><c:out value="${r.origen} → ${r.destino}"/></td>
          <td class="num"><fmt:formatNumber value="${r.km}" pattern="#,##0.#"/></td><td><c:out value="${r.duracion}"/></td><td><t:badge estado="${r.estado}"/></td>
          <td><div class="actions"><form class="inline" method="post" action="${ctx}/rutas"><input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="accion" value="estado"><input type="hidden" name="id" value="${r.id}">
                <select name="estado" onchange="if(this.value)this.form.submit()" style="width:auto;padding:5px 8px;font-size:13px"><option value="">Cambiar…</option><option value="Activa">Activa</option><option value="Inactiva">Inactiva</option></select></form></div></td></tr></c:forEach>
        <c:if test="${empty rutas}"><tr><td colspan="6" class="empty">Sin rutas.</td></tr></c:if></tbody></table></div>
      <h2 style="margin-top:26px">Terminales</h2>
      <div class="table-wrap"><table><thead><tr><th>Nombre</th><th>Ciudad</th><th>Teléfono</th><th>Estado</th></tr></thead><tbody>
        <c:forEach items="${terminales}" var="x"><tr><td><c:out value="${x.nombre}"/></td><td><c:out value="${x.ciudad}"/></td><td><c:out value="${x.telefono}"/></td><td><t:badge estado="${x.estado}"/></td></tr></c:forEach>
      </tbody></table></div>
    </section>
  </div>
</t:layout>
