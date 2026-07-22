import { useEffect, useState } from 'react'
import { api, moneda } from '../api.js'
import Distintivo from '../components/Distintivo.jsx'

const CATEGORIAS = ['SNORKEL', 'KAYAK', 'PADDLE_BOARD', 'JET_SKI', 'CATAMARAN', 'BUCEO', 'TOUR_GUIADO']

const vacio = {
  nombre: '', descripcion: '', categoria: 'SNORKEL', duracionMinutos: 60,
  precioPorPersona: '', capacidadMaxima: 10, puntoEncuentro: ''
}

export default function Experiencias() {
  const [actividades, setActividades] = useState([])
  const [formulario, setFormulario] = useState(vacio)
  const [abierto, setAbierto] = useState(false)
  const [error, setError] = useState('')
  const [exito, setExito] = useState('')

  const cargar = () => api.actividades().then(setActividades).catch((e) => setError(e.message))
  useEffect(() => { cargar() }, [])

  const cambiar = (campo) => (e) => setFormulario({ ...formulario, [campo]: e.target.value })

  const guardar = async () => {
    setError(''); setExito('')
    try {
      await api.crearActividad({
        ...formulario,
        duracionMinutos: Number(formulario.duracionMinutos),
        capacidadMaxima: Number(formulario.capacidadMaxima),
        precioPorPersona: Number(formulario.precioPorPersona),
        activa: true
      })
      setFormulario(vacio); setAbierto(false)
      setExito('Experiencia publicada en el catalogo.')
      cargar()
    } catch (e) { setError(e.message) }
  }

  const retirar = async (id) => {
    setError(''); setExito('')
    try {
      await api.retirarActividad(id)
      setExito('La experiencia salio del catalogo activo.')
      cargar()
    } catch (e) { setError(e.message) }
  }

  return (
    <>
      <header className="encabezado">
        <h1>Experiencias</h1>
        <p>Catalogo de salidas acuaticas que la microempresa comercializa, con su duracion, cupo maximo por salida y tarifa por persona.</p>
      </header>

      <div className="acciones">
        <button className="boton" onClick={() => setAbierto(!abierto)}>
          {abierto ? 'Cerrar formulario' : 'Publicar experiencia'}
        </button>
      </div>

      {error && <div className="aviso error">{error}</div>}
      {exito && <div className="aviso exito">{exito}</div>}

      {abierto && (
        <section className="panel">
          <h2>Nueva experiencia</h2>
          <p className="ayuda">Los datos alimentan el calculo de cupos y el importe de cada reserva.</p>
          <div className="formulario">
            <div className="campo">
              <label htmlFor="nombre">Nombre</label>
              <input id="nombre" value={formulario.nombre} onChange={cambiar('nombre')} placeholder="Snorkel en Boca Catalina" />
            </div>
            <div className="campo">
              <label htmlFor="categoria">Categoria</label>
              <select id="categoria" value={formulario.categoria} onChange={cambiar('categoria')}>
                {CATEGORIAS.map((c) => <option key={c} value={c}>{c.replace(/_/g, ' ')}</option>)}
              </select>
            </div>
            <div className="campo">
              <label htmlFor="duracion">Duracion (minutos)</label>
              <input id="duracion" type="number" min="15" value={formulario.duracionMinutos} onChange={cambiar('duracionMinutos')} />
            </div>
            <div className="campo">
              <label htmlFor="precio">Precio por persona (USD)</label>
              <input id="precio" type="number" min="1" step="0.01" value={formulario.precioPorPersona} onChange={cambiar('precioPorPersona')} placeholder="45.00" />
            </div>
            <div className="campo">
              <label htmlFor="capacidad">Cupos por salida</label>
              <input id="capacidad" type="number" min="1" value={formulario.capacidadMaxima} onChange={cambiar('capacidadMaxima')} />
            </div>
            <div className="campo">
              <label htmlFor="punto">Punto de encuentro</label>
              <input id="punto" value={formulario.puntoEncuentro} onChange={cambiar('puntoEncuentro')} placeholder="Palm Beach Pier" />
            </div>
            <div className="campo ancho">
              <label htmlFor="descripcion">Descripcion</label>
              <textarea id="descripcion" rows="3" value={formulario.descripcion} onChange={cambiar('descripcion')}
                        placeholder="Que incluye la salida, nivel requerido y que debe llevar el visitante." />
            </div>
          </div>
          <div className="acciones">
            <button className="boton coral" onClick={guardar}>Publicar experiencia</button>
            <button className="boton tenue" onClick={() => { setAbierto(false); setFormulario(vacio) }}>Descartar</button>
          </div>
        </section>
      )}

      <section className="rejilla" style={{ marginTop: 26 }}>
        {actividades.map((a) => (
          <article className="experiencia" key={a.id}>
            <div>
              <Distintivo valor={a.categoria} tipo="categoria" />
              {!a.activa && <Distintivo valor="baja" />}
            </div>
            <h3>{a.nombre}</h3>
            <p className="resumen">{a.descripcion || 'Sin descripcion registrada.'}</p>
            <div className="meta">
              <span>{a.duracionMinutos} min</span>
              <span>{a.capacidadMaxima} cupos</span>
              <span>{a.puntoEncuentro || 'Punto por definir'}</span>
            </div>
            <div className="precio">{moneda(a.precioPorPersona)} <small>por persona</small></div>
            {a.activa && (
              <button className="boton tenue mini" onClick={() => retirar(a.id)}>Retirar del catalogo</button>
            )}
          </article>
        ))}
      </section>
    </>
  )
}
