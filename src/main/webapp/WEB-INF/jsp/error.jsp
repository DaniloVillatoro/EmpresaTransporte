<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Error · Transporte HN</title><link rel="stylesheet" href="${ctx}/css/app.css"></head>
<body class="login-body">
<div class="login">
  <h2 style="margin-top:0">Algo salió mal</h2>
  <p>No se pudo completar la operación. Si el problema es la conexión, revisa <b>db.properties</b> (servidor, usuario y clave de SQL Server) y que el servicio esté iniciado.</p>
  <c:if test="${not empty detalleError}"><div class="flash error"><c:out value="${detalleError}"/></div></c:if>
  <a class="btn block" href="${ctx}/panel">Volver al panel</a>
</div></body></html>
