<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="volver" required="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form method="post" action="${ctx}/boletos" data-venta="${ctx}/boletos" autocomplete="off">
  <input type="hidden" name="_csrf" value="${csrf}">
  <input type="hidden" name="accion" value="vender">
  <input type="hidden" name="volver" value="${volver}">
  <input type="hidden" name="idDisponibilidad" value="">

  <input type="text" name="identidad" placeholder="Identidad del cliente" required maxlength="20" style="margin-top:4px">
  <div class="row2" style="margin-top:10px">
    <input type="text" name="nombre" placeholder="Nombre (si es nuevo)" maxlength="60">
    <input type="text" name="apellido" placeholder="Apellido (si es nuevo)" maxlength="60">
  </div>

  <label for="idHorario">Horario</label>
  <select name="idHorario" id="idHorario" required>
    <option value="">Seleccionar horario…</option>
    <c:forEach items="${horariosVenta}" var="h"><option value="${h.id}"><c:out value="${h.texto}"/></option></c:forEach>
  </select>

  <div class="seats-title">Asiento</div>
  <div class="seats"></div>
  <div class="legend"><span><i style="background:#fff"></i>Libre</span><span><i style="background:#d6e7fb"></i>Elegido</span><span><i style="background:#efeff1"></i>Ocupado</span></div>

  <div class="row2">
    <div><label for="idMetodoPago">Método de pago</label>
      <select name="idMetodoPago" id="idMetodoPago" required>
        <c:forEach items="${metodos}" var="m"><c:if test="${m.estado eq 'Activo'}"><option value="${m.idMetodoPago}"><c:out value="${m.nombre}"/></option></c:if></c:forEach>
      </select></div>
    <div><label for="precio">Precio (L)</label><input type="number" name="precio" id="precio" value="350" min="1" step="0.01" required></div>
  </div>
  <button class="btn block" type="submit">Registrar venta</button>
</form>
