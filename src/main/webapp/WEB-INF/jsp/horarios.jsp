<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:layout titulo="Horarios" activo="horarios" subtitulo="Programa salidas: al guardar se generan los asientos a la venta">
  <div class="grid even">
    <section class="panel"><h2>Programar horario</h2>
      <form method="post" action="${ctx}/horarios">
        <input type="hidden" name="_csrf" value="${csrf}">
        <label>Ruta</label>
        <select name="idRuta" required><option value="">Seleccionar…</option>
          <c:forEach items="${rutas}" var="r"><c:if test="${r.estado eq 'Activa'}"><option value="${r.id}"><c:out value="${r.origen} - ${r.destino}"/> (<c:out value="${r.nombre}"/>)</option></c:if></c:forEach></select>
        <div class="row2">
          <div><label>Bus</label><select name="idBus" required><option value="">Seleccionar…</option>
            <c:forEach items="${buses}" var="b"><c:if test="${b.estado eq 'Disponible' or b.estado eq 'En ruta'}"><option value="${b.idBus}"><c:out value="${b.placa}"/> · ${b.capacidad} asientos</option></c:if></c:forEach></select></div>
          <div><label>Conductor</label><select name="idConductor" required><option value="">Seleccionar…</option>
            <c:forEach items="${conductores}" var="c"><c:if test="${c.estado eq 'Activo'}"><option value="${c.idConductor}"><c:out value="${c.nombre} ${c.apellido}"/></option></c:if></c:forEach></select></div>
        </div>
        <div class="row2">
          <div><label>Fecha de salida</label><input type="date" name="fechaSalida" required></div>
          <div><label>Hora de salida</label><input type="time" name="horaSalida" required></div>
        </div>
        <div class="row2">
          <div><label>Fecha de llegada</label><input type="date" name="fechaLlegada" required></div>
          <div><label>Hora de llegada</label><input type="time" name="horaLlegada" required></div>
        </div>
        <button class="btn primary block" type="submit">Programar horario</button>
      </form>
    </section>
    <section class="panel"><div class="toolbar"><h2 style="margin:0">Horarios</h2><input type="search" placeholder="Buscar…" data-filtro="#tb"></div>
      <div class="table-wrap"><table id="tb">
        <thead><tr><th>Ruta</th><th>Bus</th><th>Conductor</th><th>Salida</th><th class="num">Ocupación</th><th>Estado</th><th></th></tr></thead><tbody>
        <c:forEach items="${horarios}" var="h"><tr>
          <td><c:out value="${h.ruta}"/></td><td><c:out value="${h.bus}"/></td><td><c:out value="${h.conductor}"/></td>
          <td><c:out value="${h.salida}"/></td><td class="num">${h.ocupados}/${h.capacidad}</td><td><t:badge estado="${h.estado}"/></td>
          <td><div class="actions"><form class="inline" method="post" action="${ctx}/horarios"><input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="accion" value="estado"><input type="hidden" name="id" value="${h.id}">
                <select name="estado" onchange="if(this.value)this.form.submit()" style="width:auto;padding:5px 8px;font-size:13px"><option value="">Cambiar…</option><option value="Programado">Programado</option><option value="En curso">En curso</option><option value="Finalizado">Finalizado</option><option value="Cancelado">Cancelado</option></select></form></div></td></tr></c:forEach>
        <c:if test="${empty horarios}"><tr><td colspan="7" class="empty">Sin horarios.</td></tr></c:if>
        </tbody></table></div></section>
  </div>
</t:layout>
