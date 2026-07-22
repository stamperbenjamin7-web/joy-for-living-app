/**
 * Linea de marea: representa los cupos comprometidos de una salida como el
 * nivel del agua subiendo dentro de un canal de arena.
 */
export default function Marea({ ocupados, capacidad, etiqueta = 'Cupos comprometidos' }) {
  const total = capacidad || 1
  const porcentaje = Math.min(100, Math.round((ocupados / total) * 100))
  const lleno = porcentaje >= 100

  return (
    <div className="marea">
      <div className="marea-etiqueta">
        <span>{etiqueta}</span>
        <span>{ocupados} / {capacidad}</span>
      </div>
      <div
        className="marea-canal"
        role="meter"
        aria-valuenow={ocupados}
        aria-valuemin={0}
        aria-valuemax={capacidad}
        aria-label={`${etiqueta}: ${ocupados} de ${capacidad}`}
      >
        <div className={`marea-agua${lleno ? ' lleno' : ''}`} style={{ width: `${porcentaje}%` }}>
          <svg className="marea-cresta" viewBox="0 0 10 26" preserveAspectRatio="none" aria-hidden="true">
            <path d="M0,0 C5,5 5,9 0,13 C5,17 5,21 0,26 L10,26 L10,0 Z"
                  fill="currentColor" style={{ color: 'var(--arena)' }} />
          </svg>
        </div>
      </div>
    </div>
  )
}
