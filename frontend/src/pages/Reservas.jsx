import { useEffect, useState } from 'react'
import { api, fechaLegible, moneda } from '../api.js'
import Distintivo from '../components/Distintivo.jsx'
import Marea from '../components/Marea.jsx'

const vacio = { clienteId: '', actividadId: '', fechaHora: '', numeroPersonas: 1, notas: '' }

export default function Reservas() {
  const [reservas, setReservas] = useState([])
  const [clientes, setClientes] = useState([])
  const [actividades, setActividades] = useState([])
  const [formulario, setFormulario] = useState(vacio)
  const [cupos, setCupos] = useState(null)
  const [error, setError] = useState('')
  const [exito, setExito] = useState('')

  const cargarReservas = () => api.reservas().then(setReservas).catch((e) => setError(e.message))

  useEffect(() => {
    cargarReservas()
    api.clientes().then(setClientes).catch(() => {})
    api.actividades().then((datos) => setActividades(datos.filter((a) => a.activa))).catch(() => {})
  }, [])

  // Consulta de cupos en cuanto hay experiencia y horario elegidos.
  useEffect(() => {
    if (!formulario.actividadId || !formulario.fechaHora) { setCupos(null); return }
    let vigente = true
    api.disponibilidad(formulario.actividadId, formulario.fechaHora)
      .then((d) => { if (vigente) setCupos(d) })
      .catch(() => { if (vigente) setCupos(null) })
    return () => { vigente = false }
  }, [formulario.actividadId, formulario.fechaHora])

  const cambiar = (campo) => (e) => setFormulario({ ...formulario, [campo]: e.target.value })

  const reservar = async () => {
    setError(''); setExito('')
    try {
      const creada = await api.crearReserva({
        clienteId: Number(formulario.clienteId),
        actividadId: Number(formulario.actividadId),
        fechaHora: formulario.fechaHora,
        numeroPersonas: Number(formulario.numeroPersonas),
        notas: formulario.notas || null
      })
      setExito(`Reserva ${creada.codigo} creada por ${moneda(creada.total)}.`)
      setFormulario(vacio); setCupos(null)
      cargarReservas()
    } catch (e) { setError(e.message) }
  }

  const cambiarEstado = async (id, valor) => {
    setError(''); setExito('')
    try {
      await api.cambiarEstadoReserva(id, valor)
      setExito(`Reserva marcada como ${valor.toLowerCase()}.`)
      cargarReservas()
    } catch (e) { setError(e.message) }
  }

  const listo = formulario.clienteId && formulario.actividadId && formulario.fechaHora

  return (
    <>
      <header className="encabezado">
        <h1>Reservas</h1>
        <p>Registra salidas validando los cupos disponibles en tiempo real y gobierna el ciclo de vida de cada reserva.</p>
      </header>

      <section className="panel">
        <h2>Nueva reserva</h2>
        <p className="ayuda">El sistema bloquea la venta si el horario ya tiene los cupos comprometidos.</p>

        <div className="formulario">
          <div className="campo">
            <label htmlFor="cliente">Cliente</label>
            <select id="cliente" value={formulario.clienteId} onChange={cambiar('clienteId')}>
              <option value="">Selecciona un cliente</option>
              {clientes.map((c) => <option key={c.id} value={c.id}>{c.nombreCompleto}</option>)}
            </select>
          </div>
          <div className="campo">
            <label htmlFor="actividad">Experiencia</label>
            <select id="actividad" value={formulario.actividadId} onChange={cambiar('actividadId')}>
              <option value="">Selecciona una experiencia</option>
              {actividades.map((a) => (
                <option key={a.id} value={a.id}>{a.nombre} · {moneda(a.precioPorPersona)}</option>
              ))}
            </select>
          </div>
          <div className="campo">
            <label htmlFor="fecha">Fecha y hora de salida</label>
            <input id="fecha" type="datetime-local" value={formulario.fechaHora} onChange={cambiar('fechaHora')} />
          </div>
          <div className="campo">
            <label htmlFor="personas">Personas</label>
            <input id="personas" type="number" min="1" value={formulario.numeroPersonas} onChange={cambiar('numeroPersonas')} />
          </div>
          <div className="campo ancho">
            <label htmlFor="notas">Notas para la tripulacion</label>
            <textarea id="notas" rows="2" value={formulario.notas} onChange={cambiar('notas')}
                      placeholder="Alergias, nivel de experiencia, traslado solicitado." />
          </div>
        </div>

        {cupos && (
          <div style={{ marginTop: 18 }}>
            <Marea ocupados={cupos.cuposOcupados} capacidad={cupos.capacidadMaxima}
                   etiqueta={`Cupos de ${cupos.actividadNombre} en ese horario`} />
            {!cupos.hayCupo && (
              <div className="aviso dato">Ese horario esta completo. Elige otra hora para esta experiencia.</div>
            )}
          </div>
        )}

        <div className="acciones">
          <button className="boton coral" onClick={reservar} disabled={!listo}>Crear reserva</button>
          {!listo && <span className="cargando">Elige cliente, experiencia y horario.</span>}
        </div>

        {error && <div className="aviso error">{error}</div>}
        {exito && <div className="aviso exito">{exito}</div>}
      </section>

      <section className="panel">
        <h2>Reservas registradas</h2>
        <p className="ayuda">De la mas reciente a la mas antigua segun la fecha de salida.</p>
        {reservas.length === 0 ? (
          <div className="vacio">
            <h3>Aun no hay reservas</h3>
            <p>Crea la primera con el formulario de arriba.</p>
          </div>
        ) : (
          <table className="tabla">
            <thead>
              <tr>
                <th>Codigo</th><th>Cliente</th><th>Experiencia</th><th>Salida</th>
                <th>Pax</th><th>Total</th><th>Estado</th><th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {reservas.map((r) => (
                <tr key={r.id}>
                  <td className="codigo">{r.codigo}</td>
                  <td>{r.clienteNombre}</td>
                  <td>{r.actividadNombre}</td>
                  <td>{fechaLegible(r.fechaHora)}</td>
                  <td>{r.numeroPersonas}</td>
                  <td>{moneda(r.total)}</td>
                  <td><Distintivo valor={r.estado} /></td>
                  <td>
                    <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
                      {r.estado === 'PENDIENTE' && (
                        <button className="boton mini" onClick={() => cambiarEstado(r.id, 'CONFIRMADA')}>Confirmar</button>
                      )}
                      {r.estado === 'CONFIRMADA' && (
                        <button className="boton mini" onClick={() => cambiarEstado(r.id, 'COMPLETADA')}>Completar</button>
                      )}
                      {(r.estado === 'PENDIENTE' || r.estado === 'CONFIRMADA') && (
                        <button className="boton tenue mini" onClick={() => cambiarEstado(r.id, 'CANCELADA')}>Cancelar</button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </>
  )
}
