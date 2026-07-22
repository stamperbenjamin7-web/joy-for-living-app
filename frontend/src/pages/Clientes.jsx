import { useEffect, useState } from 'react'
import { api } from '../api.js'

const vacio = { nombre: '', apellido: '', email: '', telefono: '', pais: '' }

export default function Clientes() {
  const [clientes, setClientes] = useState([])
  const [busqueda, setBusqueda] = useState('')
  const [formulario, setFormulario] = useState(vacio)
  const [error, setError] = useState('')
  const [exito, setExito] = useState('')

  const cargar = (texto = '') => api.clientes(texto).then(setClientes).catch((e) => setError(e.message))
  useEffect(() => { cargar() }, [])

  useEffect(() => {
    const temporizador = setTimeout(() => cargar(busqueda), 300)
    return () => clearTimeout(temporizador)
  }, [busqueda])

  const cambiar = (campo) => (e) => setFormulario({ ...formulario, [campo]: e.target.value })

  const registrar = async () => {
    setError(''); setExito('')
    try {
      const creado = await api.crearCliente(formulario)
      setExito(`${creado.nombreCompleto} quedo registrado.`)
      setFormulario(vacio)
      cargar(busqueda)
    } catch (e) { setError(e.message) }
  }

  const eliminar = async (id) => {
    setError(''); setExito('')
    try {
      await api.eliminarCliente(id)
      setExito('Cliente eliminado del registro.')
      cargar(busqueda)
    } catch (e) { setError(e.message) }
  }

  return (
    <>
      <header className="encabezado">
        <h1>Clientes</h1>
        <p>Registro de visitantes que han contratado experiencias, base para el seguimiento comercial de la microempresa.</p>
      </header>

      <section className="panel">
        <h2>Registrar cliente</h2>
        <p className="ayuda">El correo identifica al cliente y no puede repetirse.</p>
        <div className="formulario">
          <div className="campo">
            <label htmlFor="c-nombre">Nombre</label>
            <input id="c-nombre" value={formulario.nombre} onChange={cambiar('nombre')} />
          </div>
          <div className="campo">
            <label htmlFor="c-apellido">Apellido</label>
            <input id="c-apellido" value={formulario.apellido} onChange={cambiar('apellido')} />
          </div>
          <div className="campo">
            <label htmlFor="c-email">Correo</label>
            <input id="c-email" type="email" value={formulario.email} onChange={cambiar('email')} />
          </div>
          <div className="campo">
            <label htmlFor="c-telefono">Telefono</label>
            <input id="c-telefono" value={formulario.telefono} onChange={cambiar('telefono')} placeholder="+297 560 0000" />
          </div>
          <div className="campo">
            <label htmlFor="c-pais">Pais de origen</label>
            <input id="c-pais" value={formulario.pais} onChange={cambiar('pais')} />
          </div>
        </div>
        <div className="acciones">
          <button className="boton coral" onClick={registrar}>Registrar cliente</button>
        </div>
        {error && <div className="aviso error">{error}</div>}
        {exito && <div className="aviso exito">{exito}</div>}
      </section>

      <section className="panel">
        <h2>Cartera de clientes</h2>
        <p className="ayuda">Busca por nombre, apellido o correo.</p>
        <input className="buscador" value={busqueda} onChange={(e) => setBusqueda(e.target.value)}
               placeholder="Buscar cliente" aria-label="Buscar cliente" />
        {clientes.length === 0 ? (
          <div className="vacio">
            <h3>Sin resultados</h3>
            <p>Ajusta la busqueda o registra un cliente nuevo.</p>
          </div>
        ) : (
          <table className="tabla">
            <thead>
              <tr><th>Cliente</th><th>Correo</th><th>Telefono</th><th>Pais</th><th>Registro</th><th></th></tr>
            </thead>
            <tbody>
              {clientes.map((c) => (
                <tr key={c.id}>
                  <td>{c.nombreCompleto}</td>
                  <td>{c.email}</td>
                  <td>{c.telefono || '—'}</td>
                  <td>{c.pais || '—'}</td>
                  <td className="codigo">{c.fechaRegistro}</td>
                  <td><button className="boton tenue mini" onClick={() => eliminar(c.id)}>Eliminar</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </>
  )
}
