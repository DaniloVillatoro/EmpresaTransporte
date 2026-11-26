<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="estado" required="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:choose>
  <c:when test="${estado eq 'Programado' or estado eq 'Activo' or estado eq 'Activa' or estado eq 'Disponible' or estado eq 'Aprobado' or estado eq 'Pagado' or estado eq 'Finalizado' or estado eq 'Procesado'}"><c:set var="cls" value="b-green"/></c:when>
  <c:when test="${estado eq 'En curso' or estado eq 'En ruta' or estado eq 'En proceso'}"><c:set var="cls" value="b-blue"/></c:when>
  <c:when test="${estado eq 'Cancelado' or estado eq 'Inactivo' or estado eq 'Inactiva' or estado eq 'Rechazado'}"><c:set var="cls" value="b-red"/></c:when>
  <c:when test="${estado eq 'Mantenimiento' or estado eq 'Pendiente' or estado eq 'Reembolsado' or estado eq 'Solicitado'}"><c:set var="cls" value="b-amber"/></c:when>
  <c:otherwise><c:set var="cls" value="b-gray"/></c:otherwise>
</c:choose>
<span class="badge ${cls}"><c:out value="${estado}"/></span>
