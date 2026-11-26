<%@ tag language="java" pageEncoding="UTF-8" body-content="scriptless" %>
<%@ attribute name="titulo" required="true" %>
<%@ attribute name="activo" required="true" %>
<%@ attribute name="subtitulo" required="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:out value="${titulo}"/> · Transporte HN</title>
  <link rel="stylesheet" href="${ctx}/css/app.css">
</head>
<body>
<svg width="0" height="0" style="position:absolute" aria-hidden="true">
  <symbol id="i-bus" viewBox="0 0 24 24"><path d="M8 6v6M16 6v6M2 12h19.6M18 18h3s.5-1.7.8-2.8c.1-.4.2-.8.2-1.2 0-.4-.1-.8-.2-1.2l-1.4-5C20.1 6.8 19.1 6 18 6H4a2 2 0 0 0-2 2v10h3"/><circle cx="7" cy="18" r="2"/><path d="M9 18h5"/><circle cx="16" cy="18" r="2"/></symbol>
  <symbol id="i-panel" viewBox="0 0 24 24"><rect x="3" y="3" width="7" height="9" rx="1"/><rect x="14" y="3" width="7" height="5" rx="1"/><rect x="14" y="12" width="7" height="9" rx="1"/><rect x="3" y="16" width="7" height="5" rx="1"/></symbol>
  <symbol id="i-ticket" viewBox="0 0 24 24"><path d="M2 9a3 3 0 0 1 0 6v2a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-2a3 3 0 0 1 0-6V7a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2Z"/><path d="M13 5v2M13 17v2M13 11v2"/></symbol>
  <symbol id="i-cal" viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="18" rx="2"/><path d="M16 2v4M8 2v4M3 10h18"/></symbol>
  <symbol id="i-route" viewBox="0 0 24 24"><circle cx="6" cy="19" r="3"/><path d="M9 19h8.5a3.5 3.5 0 0 0 0-7h-11a3.5 3.5 0 0 1 0-7H15"/><circle cx="18" cy="5" r="3"/></symbol>
  <symbol id="i-wheel" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"/><circle cx="12" cy="12" r="3"/><path d="M12 15v7M9.5 10.5 3 9M14.5 10.5 21 9"/></symbol>
  <symbol id="i-users" viewBox="0 0 24 24"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"/></symbol>
  <symbol id="i-card" viewBox="0 0 24 24"><rect x="2" y="5" width="20" height="14" rx="2"/><path d="M2 10h20"/></symbol>
  <symbol id="i-wrench" viewBox="0 0 24 24"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"/></symbol>
  <symbol id="i-history" viewBox="0 0 24 24"><path d="M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8"/><path d="M3 3v5h5"/><path d="M12 7v5l4 2"/></symbol>
  <symbol id="i-user" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"/><circle cx="12" cy="10" r="3"/><path d="M7 20.66V19a2 2 0 0 1 2-2h6a2 2 0 0 1 2 2v1.66"/></symbol>
  <symbol id="i-file" viewBox="0 0 24 24"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><path d="M14 2v6h6M8 13h8M8 17h8"/></symbol>
</svg>

<div class="app">
  <aside class="sidebar">
    <div class="brand"><svg class="ico"><use href="#i-bus"/></svg> Transporte HN</div>
    <nav class="nav">
      <a href="${ctx}/panel" class="${activo eq 'panel' ? 'active' : ''}"><svg class="ico"><use href="#i-panel"/></svg>Panel</a>
      <a href="${ctx}/boletos" class="${activo eq 'boletos' ? 'active' : ''}"><svg class="ico"><use href="#i-ticket"/></svg>Boletos</a>
      <a href="${ctx}/horarios" class="${activo eq 'horarios' ? 'active' : ''}"><svg class="ico"><use href="#i-cal"/></svg>Horarios</a>
      <a href="${ctx}/rutas" class="${activo eq 'rutas' ? 'active' : ''}"><svg class="ico"><use href="#i-route"/></svg>Rutas</a>
      <a href="${ctx}/buses" class="${activo eq 'buses' ? 'active' : ''}"><svg class="ico"><use href="#i-bus"/></svg>Buses</a>
      <a href="${ctx}/conductores" class="${activo eq 'conductores' ? 'active' : ''}"><svg class="ico"><use href="#i-wheel"/></svg>Conductores</a>
      <a href="${ctx}/clientes" class="${activo eq 'clientes' ? 'active' : ''}"><svg class="ico"><use href="#i-users"/></svg>Clientes</a>
      <a href="${ctx}/pagos" class="${activo eq 'pagos' ? 'active' : ''}"><svg class="ico"><use href="#i-card"/></svg>Pagos</a>
      <a href="${ctx}/mantenimiento" class="${activo eq 'mantenimiento' ? 'active' : ''}"><svg class="ico"><use href="#i-wrench"/></svg>Mantenimiento</a>
      <c:if test="${sessionScope.usuario.cargo eq 'Administrador'}">
        <div class="sep"></div>
        <a href="${ctx}/reportes" class="${activo eq 'reportes' ? 'active' : ''}"><svg class="ico"><use href="#i-file"/></svg>Reportes</a>
        <a href="${ctx}/auditoria" class="${activo eq 'auditoria' ? 'active' : ''}"><svg class="ico"><use href="#i-history"/></svg>Auditoría</a>
      </c:if>
    </nav>
    <div class="side-foot">v2.0 · Java Web · MVC</div>
  </aside>

  <main class="main">
    <header class="top">
      <div>
        <h1><c:out value="${titulo}"/></h1>
        <c:if test="${not empty subtitulo}"><p><c:out value="${subtitulo}"/></p></c:if>
      </div>
      <div class="user">
        <svg class="ico"><use href="#i-user"/></svg>
        <span><c:out value="${sessionScope.usuario.cargo}"/>: <strong><c:out value="${sessionScope.usuario.nombre} ${sessionScope.usuario.apellido}"/></strong></span>
        <a href="${ctx}/logout">Salir</a>
      </div>
    </header>

    <c:if test="${not empty flashMsg}">
      <div class="flash ${flashTipo}">
        <span><c:out value="${flashMsg}"/></span>
        <c:if test="${flashTipo eq 'ok' and not empty ultimoBoleto}">
          <a href="${ctx}/reportes?tipo=boleto&id=${ultimoBoleto}" target="_blank" rel="noopener">Imprimir boleto</a>
        </c:if>
      </div>
    </c:if>

    <jsp:doBody/>
  </main>
</div>
<script src="${ctx}/js/app.js"></script>
</body>
</html>
