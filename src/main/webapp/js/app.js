/* Transporte HN - Lógica multipágina conectada a API REST */
(function () {
  'use strict';

  var API = (typeof CONFIG !== 'undefined' && CONFIG.API_BASE)
    ? CONFIG.API_BASE
    : 'https://empresatransporte.onrender.com/mavenproject3/resources';

  var paginaActual = (window.location.pathname.split('/').pop() || 'index.html').toLowerCase();
  var esPaginaPublica = paginaActual === '' ||
    paginaActual === 'index.html' ||
    paginaActual === 'login.html' ||
    paginaActual === 'error.html';

  function mostrarFlash(mensaje, tipo) {
    if (!tipo) tipo = 'ok';
    var cont = document.getElementById('flashContainer');
    if (!cont) return;
    cont.innerHTML = '<div class="flash ' + tipo + '"><span>' + mensaje + '</span></div>';
  }

  function obtenerSesion() {
    try {
      return JSON.parse(sessionStorage.getItem('transporte_sesion') || 'null');
    } catch (_) {
      return null;
    }
  }

  function guardarSesion(data) {
    sessionStorage.setItem('transporte_sesion', JSON.stringify(data));
  }

  function limpiarSesion() {
    sessionStorage.removeItem('transporte_sesion');
  }

  function headersApi() {
    var h = { 'Content-Type': 'application/json' };
    var sesion = obtenerSesion();
    if (sesion && sesion.token) {
      h['Authorization'] = 'Bearer ' + sesion.token;
    }
    return h;
  }

  async function apiFetch(ruta, opciones) {
    opciones = opciones || {};
    var resp = await fetch(API + ruta, Object.assign({
      headers: headersApi()
    }, opciones));

    if (!resp.ok) {
      var detalle = 'Error HTTP ' + resp.status;
      try {
        var errJson = await resp.json();
        detalle = errJson.mensaje || errJson.error || detalle;
      } catch (_) {}
      throw new Error(detalle);
    }

    var texto = await resp.text();
    return texto ? JSON.parse(texto) : null;
  }

  // ---- Sesión y cabecera ----
  var sesion = obtenerSesion();
  if (!esPaginaPublica && !sesion) {
    window.location.replace('login.html');
    return;
  }

  var spanUsuario = document.getElementById('usuarioActual');
  if (spanUsuario && sesion) {
    var partesNombre = [sesion.nombre, sesion.apellido].filter(Boolean);
    var nombreMostrar = partesNombre.length ? partesNombre.join(' ') : (sesion.usuario || 'Empleado');
    spanUsuario.textContent = sesion.cargo ? (sesion.cargo + ': ' + nombreMostrar) : nombreMostrar;
  }

  var btnLogout = document.getElementById('btnLogout');
  if (btnLogout) {
    btnLogout.addEventListener('click', function (e) {
      e.preventDefault();
      limpiarSesion();
      window.location.href = 'login.html';
    });
  }

  // ---- Buscador de tablas: <input data-filtro="#idTabla"> ----
  document.querySelectorAll('input[data-filtro]').forEach(function (inp) {
    inp.addEventListener('input', function () {
      var q = inp.value.toLowerCase();
      document.querySelectorAll(inp.dataset.filtro + ' tbody tr').forEach(function (tr) {
        tr.style.display = tr.textContent.toLowerCase().indexOf(q) > -1 ? '' : 'none';
      });
    });
  });

  // ---- Confirmaciones: <form data-confirmar="mensaje"> ----
  document.querySelectorAll('form[data-confirmar]').forEach(function (f) {
    f.addEventListener('submit', function (e) {
      if (!confirm(f.dataset.confirmar)) e.preventDefault();
    });
  });

  // ---- Login ----
  var formLogin = document.getElementById('formLogin');
  if (formLogin) {
    formLogin.addEventListener('submit', async function (e) {
      e.preventDefault();
      var errBox = document.getElementById('loginError');
      if (errBox) errBox.style.display = 'none';

      var payload = {
        usuario: formLogin.usuario.value.trim(),
        clave: formLogin.clave.value
      };

      try {
        var res = await apiFetch('/login', {
          method: 'POST',
          body: JSON.stringify(payload)
        });
        guardarSesion(res || { usuario: payload.usuario });
        window.location.href = 'dashboard.html';
      } catch (err) {
        if (errBox) {
          errBox.textContent = err.message || 'Credenciales inválidas.';
          errBox.style.display = 'flex';
        }
      }
    });
  }

  // ---- Buses ----
  var tbodyBuses = document.getElementById('tbodyBuses');
  async function cargarBuses() {
    if (!tbodyBuses) return;
    try {
      var lista = await apiFetch('/buses');
      if (!Array.isArray(lista) || !lista.length) {
        tbodyBuses.innerHTML = '<tr><td colspan="6" class="empty">Sin buses.</td></tr>';
        return;
      }
      tbodyBuses.innerHTML = lista.map(function (b) {
        var unidad = [b.marca, b.modelo, b.anio].filter(Boolean).join(' ');
        var tipo = b.tipoBus || b.tipo || '';
        var estado = b.estado || 'Disponible';
        return '<tr>' +
          '<td class="mono">' + (b.placa || '') + '</td>' +
          '<td>' + unidad + '</td>' +
          '<td class="num">' + (b.capacidad != null ? b.capacidad : '') + '</td>' +
          '<td>' + tipo + '</td>' +
          '<td><span class="badge b-green">' + estado + '</span></td>' +
          '<td></td>' +
        '</tr>';
      }).join('');
    } catch (err) {
      mostrarFlash('No se pudieron cargar los buses: ' + err.message, 'error');
    }
  }

  var formBus = document.getElementById('formBus');
  if (formBus) {
    formBus.addEventListener('submit', async function (e) {
      e.preventDefault();
      var data = Object.fromEntries(new FormData(formBus).entries());
      try {
        await apiFetch('/buses', {
          method: 'POST',
          body: JSON.stringify(data)
        });
        formBus.reset();
        mostrarFlash('Bus guardado correctamente.');
        cargarBuses();
      } catch (err) {
        mostrarFlash('Error al guardar bus: ' + err.message, 'error');
      }
    });
  }

  // ---- Dashboard ----
  var tbodySalidas = document.getElementById('tbodySalidas');
  var chartSemana = document.getElementById('chartSemana');

  function renderIndicadores(ind) {
    ind = ind || {};
    var elBoletos = document.getElementById('statBoletosHoy');
    var elVentas = document.getElementById('statVentasHoy');
    var elRuta = document.getElementById('statBusesEnRuta');
    var elMant = document.getElementById('statEnMantenimiento');

    if (elBoletos) elBoletos.textContent = ind.boletoshoy != null ? ind.boletoshoy : 0;
    if (elVentas) {
      var monto = Number(ind.ventashoy || 0);
      elVentas.textContent = 'L ' + monto.toLocaleString('es-HN', { minimumFractionDigits: 0, maximumFractionDigits: 2 });
    }
    if (elRuta) elRuta.textContent = ind.busesenruta != null ? ind.busesenruta : 0;
    if (elMant) elMant.textContent = ind.enmantenimiento != null ? ind.enmantenimiento : 0;
  }

  function renderSalidas(salidas) {
    if (!tbodySalidas) return;
    if (!Array.isArray(salidas) || !salidas.length) {
      tbodySalidas.innerHTML = '<tr><td colspan="5" class="empty">No hay salidas programadas.</td></tr>';
      return;
    }
    tbodySalidas.innerHTML = salidas.map(function (s) {
      var horaTxt = [s.fecha, s.hora].filter(Boolean).join(' ');
      return '<tr>' +
        '<td>' + (s.ruta || '') + '</td>' +
        '<td>' + horaTxt + '</td>' +
        '<td class="mono">' + (s.bus || '') + '</td>' +
        '<td class="num">' + (s.libres != null ? s.libres : 0) + '</td>' +
        '<td><span class="badge b-green">' + (s.estado || '') + '</span></td>' +
        '</tr>';
    }).join('');
  }

  function renderSemana(semana) {
    if (!chartSemana) return;
    if (!Array.isArray(semana) || !semana.length) {
      chartSemana.innerHTML = '<div class="empty">Sin datos de ventas.</div>';
      return;
    }
    var maxVal = semana.reduce(function (acc, item) {
      var val = Number(item.total || 0);
      return val > acc ? val : acc;
    }, 0);

    chartSemana.innerHTML = semana.map(function (d) {
      var total = Number(d.total || 0);
      var pct = maxVal > 0 ? Math.max(4, Math.round((total / maxVal) * 100)) : 4;
      return '<div class="bar-col" title="L ' + total.toFixed(2) + '">' +
        '<div class="bar-fill" style="height:' + pct + '%"></div>' +
        '<small>' + (d.dia || '') + '</small>' +
        '</div>';
    }).join('');
  }

  async function cargarDashboard() {
    if (!tbodySalidas && !chartSemana && !document.getElementById('statBoletosHoy')) return;
    try {
      var data = await apiFetch('/dashboard');
      if (!data) return;
      renderIndicadores(data.indicadores);
      renderSalidas(data.salidas);
      renderSemana(data.semana);
    } catch (err) {
      mostrarFlash('No se pudo cargar el panel: ' + err.message, 'error');
    }
  }

  // ---- Módulos operativos ----
  function badgeModulo(estado) {
    var est = String(estado || '').toLowerCase();
    if (est === 'disponible' || est === 'pagado' || est === 'activo' || est === 'programado') {
      return '<span class="badge b-green">' + (estado || '') + '</span>';
    }
    if (est === 'reembolsado' || est === 'anulado' || est === 'en mantenimiento' || est === 'inactivo') {
      return '<span class="badge b-amber">' + (estado || '') + '</span>';
    }
    return '<span class="badge">' + (estado || '') + '</span>';
  }

  function renderHorarios(lista) {
    var tb = document.getElementById('tbodyHorarios');
    if (!tb) return;
    if (!Array.isArray(lista) || !lista.length) {
      tb.innerHTML = '<tr><td colspan="7" class="empty">Sin horarios.</td></tr>';
      return;
    }
    tb.innerHTML = lista.map(function (h) {
      var ocup = h.ocupados != null ? h.ocupados : 0;
      var cap = h.capacidad != null ? h.capacidad : 0;
      var salidaTxt = [h.fechaSalida || h.fecha, h.horaSalida || h.hora].filter(Boolean).join(' ') || (h.salida || '');
      return '<tr>' +
        '<td>' + (h.ruta || '') + '</td>' +
        '<td class="mono">' + (h.bus || '') + '</td>' +
        '<td>' + (h.conductor || '') + '</td>' +
        '<td>' + salidaTxt + '</td>' +
        '<td class="num">' + ocup + ' / ' + cap + '</td>' +
        '<td>' + badgeModulo(h.estado) + '</td>' +
        '<td></td>' +
        '</tr>';
    }).join('');
  }

  function renderRutas(lista) {
    var tb = document.getElementById('tbodyRutas');
    if (!tb) return;
    if (!Array.isArray(lista) || !lista.length) {
      tb.innerHTML = '<tr><td colspan="6" class="empty">Sin rutas.</td></tr>';
      return;
    }
    tb.innerHTML = lista.map(function (r) {
      var tramo = r.tramo || [r.origen, r.destino].filter(Boolean).join(' → ');
      var kmTxt = r.km != null ? (Number(r.km).toFixed(2) + ' km') : (r.distanciaKm != null ? (Number(r.distanciaKm).toFixed(2) + ' km') : '');
      return '<tr>' +
        '<td>' + (r.nombre || r.nombreRuta || '') + '</td>' +
        '<td>' + tramo + '</td>' +
        '<td class="num">' + kmTxt + '</td>' +
        '<td>' + (r.duracion || r.duracionEstimada || '') + '</td>' +
        '<td>' + badgeModulo(r.estado) + '</td>' +
        '<td></td>' +
        '</tr>';
    }).join('');
  }

  function renderTerminales(lista) {
    var tb = document.getElementById('tbodyTerminales');
    if (!tb) return;
    if (!Array.isArray(lista) || !lista.length) {
      tb.innerHTML = '<tr><td colspan="4" class="empty">Sin terminales.</td></tr>';
      return;
    }
    tb.innerHTML = lista.map(function (t) {
      return '<tr>' +
        '<td>' + (t.nombre || '') + '</td>' +
        '<td>' + (t.ciudad || '') + '</td>' +
        '<td>' + (t.telefono || '') + '</td>' +
        '<td>' + badgeModulo(t.estado) + '</td>' +
        '</tr>';
    }).join('');
  }

  function renderClientes(lista) {
    var tb = document.getElementById('tbodyClientes');
    if (!tb) return;
    if (!Array.isArray(lista) || !lista.length) {
      tb.innerHTML = '<tr><td colspan="5" class="empty">Sin clientes.</td></tr>';
      return;
    }
    tb.innerHTML = lista.map(function (c) {
      var nom = [c.nombre, c.apellido].filter(Boolean).join(' ');
      return '<tr>' +
        '<td>' + nom + '</td>' +
        '<td class="mono">' + (c.identidad || '') + '</td>' +
        '<td>' + (c.telefono || '') + '</td>' +
        '<td>' + badgeModulo(c.estado) + '</td>' +
        '<td></td>' +
        '</tr>';
    }).join('');
  }

  function renderPagos(lista) {
    var tb = document.getElementById('tbodyPagos');
    if (!tb) return;
    if (!Array.isArray(lista) || !lista.length) {
      tb.innerHTML = '<tr><td colspan="6" class="empty">Sin pagos.</td></tr>';
      return;
    }
    tb.innerHTML = lista.map(function (p) {
      var monto = Number(p.monto || 0);
      return '<tr>' +
        '<td class="mono">' + (p.boleto || p.codigoBoleto || '') + '</td>' +
        '<td>' + (p.metodo || p.metodoPago || '') + '</td>' +
        '<td class="num">L ' + monto.toFixed(2) + '</td>' +
        '<td>' + (p.fecha || '') + '</td>' +
        '<td class="mono">' + (p.referencia || '') + '</td>' +
        '<td>' + badgeModulo(p.estado) + '</td>' +
        '</tr>';
    }).join('');
  }

  function renderConductores(lista) {
    var tb = document.getElementById('tbodyConductores');
    if (!tb) return;
    if (!Array.isArray(lista) || !lista.length) {
      tb.innerHTML = '<tr><td colspan="6" class="empty">Sin conductores.</td></tr>';
      return;
    }
    tb.innerHTML = lista.map(function (c) {
      var infoExtra = c.horarios ? (' title="Horarios: ' + String(c.horarios).replace(/"/g, '&quot;') + '"') : '';
      return '<tr' + infoExtra + '>' +
        '<td>' + (c.nombre || '') + '</td>' +
        '<td class="mono">' + (c.identidad || '') + '</td>' +
        '<td class="mono">' + (c.licencia || '') + '</td>' +
        '<td>' + (c.vence || '') + '</td>' +
        '<td>' + badgeModulo(c.estado) + '</td>' +
        '<td></td>' +
        '</tr>';
    }).join('');
  }

  function renderMantenimiento(lista) {
    var tb = document.getElementById('tbodyMantenimiento');
    if (!tb) return;
    if (!Array.isArray(lista) || !lista.length) {
      tb.innerHTML = '<tr><td colspan="6" class="empty">Sin registros.</td></tr>';
      return;
    }
    tb.innerHTML = lista.map(function (m) {
      var costo = Number(m.costo || 0);
      return '<tr>' +
        '<td class="mono">' + (m.bus || m.placa || '') + '</td>' +
        '<td>' + (m.fecha || '') + '</td>' +
        '<td>' + (m.tipo || '') + '</td>' +
        '<td class="num">L ' + costo.toFixed(2) + '</td>' +
        '<td>' + badgeModulo(m.estado) + '</td>' +
        '<td></td>' +
        '</tr>';
    }).join('');
  }

  function llenarSelectTerminales(terminales) {
    var selOrigen = document.getElementById('selectOrigen');
    var selDestino = document.getElementById('selectDestino');
    if (!selOrigen && !selDestino) return;

    var lista = Array.isArray(terminales) ? terminales : [];
    var opciones = '<option value="">Seleccionar terminal…</option>' +
      lista.map(function (t) {
        var texto = t.nombre + (t.ciudad ? (' (' + t.ciudad + ')') : '');
        return '<option value="' + t.idTerminal + '">' + texto + '</option>';
      }).join('');

    if (selOrigen) selOrigen.innerHTML = opciones;
    if (selDestino) selDestino.innerHTML = opciones;
  }

  function llenarCombosOperativos(data) {
    if (!data) return;

    function llenar(id, lista, fnValor, fnTexto, placeholder) {
      var sel = document.getElementById(id);
      if (!sel) return;
      var arr = Array.isArray(lista) ? lista : [];
      sel.innerHTML = '<option value="">' + (placeholder || 'Seleccionar…') + '</option>' +
        arr.map(function (item) {
          return '<option value="' + fnValor(item) + '">' + fnTexto(item) + '</option>';
        }).join('');
    }

    llenar(
      'selectBusMantenimiento',
      data.buses,
      function (b) { return b.id; },
      function (b) { return (b.placa || '') + (b.modelo ? (' · ' + b.modelo) : ''); }
    );
    llenar(
      'selectRuta',
      data.rutas,
      function (r) { return r.id; },
      function (r) {
        var tramo = [r.origen, r.destino].filter(Boolean).join(' - ');
        return (r.nombre || '') + (tramo ? (' (' + tramo + ')') : '');
      }
    );
    llenar(
      'selectBus',
      data.buses,
      function (b) { return b.id; },
      function (b) { return (b.placa || '') + (b.capacidad ? (' (' + b.capacidad + ' as.)') : ''); }
    );
    llenar(
      'selectConductor',
      data.conductores,
      function (c) { return c.id; },
      function (c) { return c.nombre || ''; }
    );
  }
  
  // ---- Boletos ----
  var tbodyBoletos = document.getElementById('tbodyBoletos');

  function renderBoletos(lista) {
    if (!tbodyBoletos) return;
    if (!Array.isArray(lista) || !lista.length) {
      tbodyBoletos.innerHTML = '<tr><td colspan="6" class="empty">Aún no hay boletos.</td></tr>';
      return;
    }
    tbodyBoletos.innerHTML = lista.map(function (b) {
      var precioNum = Number(b.precio || 0);
      return '<tr>' +
        '<td class="mono">' + (b.codigo || '') + '</td>' +
        '<td>' + (b.cliente || '') + '</td>' +
        '<td>' + (b.ruta || '') + (b.salida ? (' · ' + b.salida) : '') + '</td>' +
        '<td class="num">' + (b.asiento != null ? b.asiento : '') + '</td>' +
        '<td class="num">L ' + precioNum.toFixed(2) + '</td>' +
        '<td><span class="badge b-green">' + (b.estado || '') + '</span></td>' +
        '</tr>';
    }).join('');
  }

  async function cargarBoletos() {
    if (!tbodyBoletos) return;
    try {
      var data = await apiFetch('/boletos');
      if (!data) return;
      renderBoletos(Array.isArray(data) ? data : data.boletos);
    } catch (err) {
      mostrarFlash('No se pudieron cargar los boletos: ' + err.message, 'error');
    }
  }

  async function cargarModulosOperativos() {
    var tieneModulo =
      document.getElementById('tbodyHorarios') ||
      document.getElementById('tbodyRutas') ||
      document.getElementById('tbodyTerminales') ||
      document.getElementById('tbodyClientes') ||
      document.getElementById('tbodyPagos') ||
      document.getElementById('tbodyMantenimiento') ||
      document.getElementById('tbodyConductores') ||
      document.getElementById('selectBusMantenimiento') ||
      document.getElementById('selectRuta') ||
      document.getElementById('selectBus') ||
      document.getElementById('selectConductor');

    if (!tieneModulo) return;

    try {
      var data = await apiFetch('/operaciones?modulo=todos');
      if (!data) return;
      renderHorarios(data.horarios);
      renderRutas(data.rutas);
      renderTerminales(data.terminales);
      renderClientes(data.clientes);
      renderPagos(data.pagos);
      renderMantenimiento(data.mantenimientos);
      renderConductores(data.conductores);
      llenarSelectTerminales(data.terminales);
      llenarCombosOperativos(data);
    } catch (err) {
      mostrarFlash('No se pudieron cargar los datos del módulo: ' + err.message, 'error');
    }
  }

  // ---- Inicialización ----
  if (tbodyBuses) cargarBuses();
  if (tbodyBoletos) cargarBoletos();
  cargarDashboard();
  cargarModulosOperativos();
})();