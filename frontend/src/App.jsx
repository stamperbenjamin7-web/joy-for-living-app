import { NavLink, Navigate, Route, Routes } from 'react-router-dom'
import Olas from './components/Olas.jsx'
import Panel from './pages/Panel.jsx'
import Experiencias from './pages/Experiencias.jsx'
import Reservas from './pages/Reservas.jsx'
import Clientes from './pages/Clientes.jsx'
import Equipos from './pages/Equipos.jsx'

const enlaces = [
  { ruta: '/panel', texto: 'Panel' },
  { ruta: '/experiencias', texto: 'Experiencias' },
  { ruta: '/reservas', texto: 'Reservas' },
  { ruta: '/clientes', texto: 'Clientes' },
  { ruta: '/equipos', texto: 'Equipamiento' }
]

export default function App() {
  return (
    <div className="app">
      <aside className="barra-lateral">
        <div className="marca">
          Joy For Living
          <span>Watersports &amp; Activities</span>
        </div>
        <div className="marca-sub">Noord · Aruba</div>

        <nav className="nav">
          {enlaces.map(({ ruta, texto }) => (
            <NavLink key={ruta} to={ruta} className={({ isActive }) => (isActive ? 'activo' : '')}>
              {texto}
            </NavLink>
          ))}
        </nav>

        <Olas />
      </aside>

      <main className="contenido">
        <Routes>
          <Route path="/" element={<Navigate to="/panel" replace />} />
          <Route path="/panel" element={<Panel />} />
          <Route path="/experiencias" element={<Experiencias />} />
          <Route path="/reservas" element={<Reservas />} />
          <Route path="/clientes" element={<Clientes />} />
          <Route path="/equipos" element={<Equipos />} />
          <Route path="*" element={<Navigate to="/panel" replace />} />
        </Routes>
      </main>
    </div>
  )
}
