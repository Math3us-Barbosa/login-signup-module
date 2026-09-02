import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import { FullPageLoader } from './FullPageLoader'

export function GuestRoute() {
  const { autenticado, carregando } = useAuth()

  if (carregando) return <FullPageLoader />
  if (autenticado) return <Navigate to="/conta" replace />
  return <Outlet />
}
