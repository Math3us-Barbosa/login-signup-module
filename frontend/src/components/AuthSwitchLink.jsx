import { Link } from 'react-router-dom'

export function AuthSwitchLink({ pergunta, texto, to }) {
  return (
    <p className="text-center text-sm text-ink/60">
      {pergunta} <Link to={to} className="font-semibold text-plum hover:text-plum-deep">{texto}</Link>
    </p>
  )
}
