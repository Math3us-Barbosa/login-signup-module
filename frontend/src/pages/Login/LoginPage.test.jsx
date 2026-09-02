import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { MemoryRouter } from 'react-router-dom'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { AuthProvider } from '../../context/AuthContext'
import * as authService from '../../services/authService'
import LoginPage from './LoginPage'

vi.mock('../../services/authService')

const { navegarMock } = vi.hoisted(() => ({ navegarMock: vi.fn() }))
vi.mock('react-router-dom', async (importOriginal) => {
  const actual = await importOriginal()
  return { ...actual, useNavigate: () => navegarMock }
})

function renderLoginPage() {
  return render(
    <MemoryRouter initialEntries={['/login']}>
      <AuthProvider>
        <LoginPage />
      </AuthProvider>
    </MemoryRouter>,
  )
}

describe('LoginPage', () => {
  beforeEach(() => {
    sessionStorage.clear()
    vi.clearAllMocks()
  })

  it('bloqueia o envio e mostra erros quando os campos estão vazios', async () => {
    const user = userEvent.setup()
    renderLoginPage()

    await user.click(screen.getByRole('button', { name: 'Entrar' }))

    expect(await screen.findByText('Email é obrigatório')).toBeInTheDocument()
    expect(screen.getByText('Senha é obrigatória')).toBeInTheDocument()
    expect(authService.login).not.toHaveBeenCalled()
  })

  it('faz login com dados válidos e navega para /conta', async () => {
    authService.login.mockResolvedValue({
      token: 'token-123',
      tipo: 'Bearer',
      expiracaoEmSegundos: 3600,
    })
    authService.buscarUsuarioLogado.mockResolvedValue({
      id: '1',
      nome: 'Maria',
      email: 'maria@exemplo.com',
      telefone: null,
      role: 'PRESTADORA',
      ativo: true,
      criadoEm: '2026-01-01T00:00:00',
    })

    const user = userEvent.setup()
    renderLoginPage()

    await user.type(screen.getByLabelText('Email'), 'maria@exemplo.com')
    await user.type(screen.getByLabelText('Senha'), 'senhaSegura123')
    await user.click(screen.getByRole('button', { name: 'Entrar' }))

    await waitFor(() =>
      expect(authService.login).toHaveBeenCalledWith({
        email: 'maria@exemplo.com',
        senha: 'senhaSegura123',
      }),
    )
    await waitFor(() => expect(navegarMock).toHaveBeenCalledWith('/conta', { replace: true }))
  })

  it('mostra a mensagem de erro geral quando a API rejeita', async () => {
    authService.login.mockRejectedValue({
      response: { status: 401, data: { detail: 'Email ou senha inválidos' } },
    })

    const user = userEvent.setup()
    renderLoginPage()

    await user.type(screen.getByLabelText('Email'), 'maria@exemplo.com')
    await user.type(screen.getByLabelText('Senha'), 'senhaErrada123')
    await user.click(screen.getByRole('button', { name: 'Entrar' }))

    expect(await screen.findByText('Email ou senha inválidos')).toBeInTheDocument()
  })
})
