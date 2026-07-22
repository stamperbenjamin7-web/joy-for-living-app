import { useEffect, useState } from 'react'
import { api } from '../api.js'
import Distintivo from '../components/Distintivo.jsx'
import Marea from '../components/Marea.jsx'

export default function Equipos() {
  const [equipos, setEquipos] = useState([])
  const [error, setError] = useState('')

  useEffect(() => { api.equipos().then(setEquipos).catch((e) => setError(e.message)) }, [])

  return (
    <>
      <header className="encabezado">
        <h1>Equipamiento</h1>
        <p>Inventario de tablas, embarcaciones y equipo de seguridad, con las unidades comprometidas frente al total disponible.</p>
      </header>

      {error && <div className="aviso error">{error}</div>}

      <section className="rejilla" style={{ marginTop: 8 }}>
        {equipos.map((e) => (
          <article className="experiencia" key={e.id}>
            <div><Distintivo valor={e.estado} /></div>
            <h3>{e.nombre}</h3>
            <p className="resumen">
              {e.actividadNombre ? `Asignado a ${e.actividadNombre}.` : 'Equipo de uso general.'}
            </p>
            <Marea ocupados={e.cantidadTotal - e.cantidadDisponible}
                   capacidad={e.cantidadTotal}
                   etiqueta="Unidades en uso" />
            <div className="meta">
              <span>{e.tipo}</span>
              <span>Mantenimiento: {e.ultimoMantenimiento || 'sin registro'}</span>
            </div>
          </article>
        ))}
      </section>

      {equipos.length === 0 && !error && (
        <div className="vacio"><h3>Inventario vacio</h3><p>Registra equipos desde el API para verlos aqui.</p></div>
      )}
    </>
  )
}
