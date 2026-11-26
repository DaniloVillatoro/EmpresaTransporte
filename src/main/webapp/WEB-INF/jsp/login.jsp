<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Ingresar · Transporte HN</title><link rel="stylesheet" href="${ctx}/css/app.css"></head>
<body class="login-body">
<form class="login" method="post" action="${ctx}/login">
  <input type="hidden" name="_csrf" value="${csrf}">
  <div class="brand"><svg class="ico" viewBox="0 0 24 24"><path d="M8 6v6M16 6v6M2 12h19.6M18 18h3s.5-1.7.8-2.8c.1-.4.2-.8.2-1.2 0-.4-.1-.8-.2-1.2l-1.4-5C20.1 6.8 19.1 6 18 6H4a2 2 0 0 0-2 2v10h3"/><circle cx="7" cy="18" r="2"/><path d="M9 18h5"/><circle cx="16" cy="18" r="2"/></svg> Transporte HN</div>
  <p>Ingresa con tu usuario de empleado.</p>
  <c:if test="${not empty error}"><div class="flash error"><c:out value="${error}"/></div></c:if>
  <label for="usuario">Usuario</label>
  <input id="usuario" name="usuario" required autofocus autocomplete="username">
  <label for="clave">Contraseña</label>
  <input id="clave" name="clave" type="password" required autocomplete="current-password">
  <button class="btn primary block" type="submit">Entrar</button>
</form>
</body></html>
