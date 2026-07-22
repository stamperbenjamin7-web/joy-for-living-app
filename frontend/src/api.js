const BASE = '/api'

async function peticion(ruta, opciones = {}) {
  const respuesta = await fetch(`${BASE}${ruta}`, {
    headers: { 'Content-Type': 'application/json' },
    ...opciones
  })

  if (respuesta.status === 204) return null

  const cuerpo = await respuesta.text()
  const datos = cuerpo ? JSON.parse(cuerpo) : null

  if (!respuesta.ok) {
    const detalle = datos?.camposInvalidos
      ? Object.values(datos.camposInvalidos).join(' ')
      : null
    throw new Error(detalle || datos?.mensaje || 'No pudimos completar la operacion.')
  }
  return datos
}

export const api = {
  resumen: () => peticion('/reportes/resumen'),

  actividades: () => peticion('/actividades'),
  crearActividad: (datos) => peticion('/actividades', { method: 'POST', body: JSON.stringify(datos) }),
  retirarActividad: (id) => peticion(`/actividades/${id}`, { method: 'DELETE' }),

  clientes: (buscar = '') => peticion(`/clientes${buscar ? `?buscar=${encodeURIComponent(buscar)}` : ''}`),
  crearCliente: (datos) => peticion('/clientes', { method: 'POST', body: JSON.stringify(datos) }),
  eliminarCliente: (id) => peticion(`/clientes/${id}`, { method: 'DELETE' }),

  equipos: () => peticion('/equipos'),

  reservas: () => peticion('/reservas'),
  crearReserva: (datos) => peticion('/reservas', { method: 'POST', body: JSON.stringify(datos) }),
  cambiarEstadoReserva: (id, valor) =>
    peticion(`/reservas/${id}/estado?valor=${valor}`, { method: 'PATCH' }),
  disponibilidad: (actividadId, fechaHora) =>
    peticion(`/reservas/disponibilidad?actividadId=${actividadId}&fechaHora=${encodeURIComponent(fechaHora)}`)
}

export const moneda = (valor) =>
  new Intl.NumberFormat('es-EC', { style: 'currency', currency: 'USD' }).format(valor ?? 0)

export const fechaLegible = (iso) => {
  if (!iso) return '—'
  return new Date(iso).toLocaleString('es-EC', {
    day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit'
  })
}
