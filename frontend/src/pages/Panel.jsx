import { useEffect, useState } from 'react'
import { api, fechaLegible, moneda } from '../api.js'
import Distintivo from '../components/Distintivo.jsx'

export default function Panel() {
  const [resumen, setResumen] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    api.resumen().then(setResumen).catch((e) => setError(e.message))
  }, [])

  if (error) return <div className="aviso error">{error}</div>
  if (!resumen) return <p className="cargando">Cargando indicadores…</p>

  const categorias = Object.entries(resumen.reservasPorCategoria || {})
  const mayor = Math.max(1, ...categorias.map(([, n]) => n))

  return (
    <>
      <header className="encabezado">
        <h1>Panel de operaciones</h1>
        <p>Estado del dia para las salidas acuaticas de Joy For Living: cupos comprometidos, ingresos confirmados y la agenda inmediata.</p>
      </header>

      <section className="indicadores">
        <div className="indicador destacado">
          <div className="cifra">{moneda(resumen.ingresosConfirmados)}</div>
          <div className="rotulo">Ingresos confirmados</div>
        </div>
        <div className="indicador">
          <div className="cifra">{resumen.totalReservas}</div>
          <div className="rotulo">Reservas registradas</div>
        </div>
        <div className="indicador">
          <div className="cifra">{resumen.reservasPendientes}</div>
          <div className="rotulo">Esperan confirmacion</div>
        </div>
        <div className="indicador">
          <div className="cifra">{resumen.totalClientes}</div>
          <div className="rotulo">Clientes en cartera</div>
        </div>
        <div className="indicador">
          <div className="cifra">{resumen.totalActividades}</div>
          <div className="rotulo">Experiencias en catalogo</div>
        </div>
      </section>

      <section className="panel">
        <h2>Proximas salidas</h2>
        <p className="ayuda">Las cinco experiencias mas cercanas en el calendario.</p>
        {resumen.proximasSalidas.length === 0 ? (
          <div className="vacio">
            <h3>Sin salidas programadas</h3>
            <p>Registra una reserva para que aparezca en la agenda.</p>
          </div>
        ) : (
          <table className="tabla">
            <thead>
              <tr>
                <th>Codigo</th><th>Experiencia</th><th>Cliente</th>
                <th>Salida</th><th>Personas</th><th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {resumen.proximasSalidas.map((r) => (
                <tr key={r.id}>
                  <td className="codigo">{r.codigo}</td>
                  <td>{r.actividadNombre}</td>
                  <td>{r.clienteNombre}</td>
                  <td>{fechaLegible(r.fechaHora)}</td>
                  <td>{r.numeroPersonas}</td>
                  <td><Distintivo valor={r.estado} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>

      <section className="panel">
        <h2>Demanda por categoria</h2>
        <p className="ayuda">Reservas vigentes agrupadas segun el tipo de experiencia.</p>
        {categorias.length === 0 ? (
          <p className="cargando">Todavia no hay reservas para analizar.</p>
        ) : (
          categorias.map(([categoria, cantidad]) => (
            <div key={categoria} className="marea" style={{ marginBottom: 14 }}>
              <div className="marea-etiqueta">
                <span>{categoria.replace(/_/g, ' ').toLowerCase()}</span>
                <span>{cantidad}</span>
              </div>
              <div className="marea-canal">
                <div className="marea-agua" style={{ width: `${(cantidad / mayor) * 100}%` }} />
              </div>
            </div>
          ))
        )}
      </section>
    </>
  )
}
