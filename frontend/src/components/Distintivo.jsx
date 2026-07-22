export default function Distintivo({ valor, tipo = '' }) {
  if (!valor) return null
  const clase = String(valor).toLowerCase()
  const texto = String(valor).replace(/_/g, ' ').toLowerCase()
  return <span className={`distintivo ${clase} ${tipo}`}>{texto}</span>
}
