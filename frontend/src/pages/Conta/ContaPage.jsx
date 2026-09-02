import { useNavigate } from 'react-router-dom'
import { Button } from '../../components/Button'
import { useAuth } from '../../hooks/useAuth'

export default function ContaPage() {
  const navigate = useNavigate()
  const { usuario, sair } = useAuth()

  const handleSair = () => {
    sair()
    navigate('/login', { replace: true })
  }

  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-6 bg-cream px-6 text-center">
      <div>
        <h1 className="font-display text-3xl font-medium text-ink">Bem-vinda, {usuario?.nome}</h1>
        <p className="mt-2 text-[15px] text-ink/60">{usuario?.email}</p>
      </div>
      <p className="max-w-sm text-sm text-ink/50">
        Ainda estamos construindo o restante da plataforma. Por enquanto, isso confirma que o
        login está funcionando de ponta a ponta.
      </p>
      <Button variant="ghost" onClick={handleSair}>
        Sair
      </Button>
    </div>
  )
}
