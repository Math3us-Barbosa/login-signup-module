import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { MemoryRouter } from 'react-router-dom'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { AuthProvider } from '../../context/AuthContext'
import * as authService from '../../services/authService'
import CadastroPage from './CadastroPage'

vi.mock('../../services/authService')

const { navegarMock } = vi.hoisted(() => ({ navegarMock: vi.fn() }))
vi.mock('react-router-dom', async (importOriginal) => {
  const actual = await importOriginal()
  return { ...actual, useNavigate: () => navegarMock }
})

function renderCadastroPage() {
  return render(
    <MemoryRouter initialEntries={['/cadastro']}>
      <AuthProvider>
        <CadastroPage />
      </AuthProvider>
    </MemoryRouter>,
  )
}

describe('CadastroPage', () => {
  beforeEach(() => {
    sessionStorage.clear()
    vi.clearAllMocks()
  })

  it('bloqueia o envio e mostra erros quando os campos obrigatórios estão vazios', async () => {
    const user = userEvent.setup()
    renderCadastroPage()

    await user.click(screen.getByRole('button', { name: 'Criar conta' }))

    expect(await screen.findByText('Nome é obrigatório')).toBeInTheDocument()
    expect(screen.getByText('Email é obrigatório')).toBeInTheDocument()
    expect(screen.getByText('Senha é obrigatória')).toBeInTheDocument()
    expect(authService.cadastrar).not.toHaveBeenCalled()
  })

  it('permite cadastro sem telefone e redireciona pro login com aviso', async () => {
    authService.cadastrar.mockResolvedValue({
      id: '1',
      nome: 'Maria',
      email: 'maria@exemplo.com',
      telefone: null,
      role: 'PRESTADORA',
      ativo: true,
      criadoEm: '2026-01-01T00:00:00',
    })

    const user = userEvent.setup()
    renderCadastroPage()

    await user.type(screen.getByLabelText('Nome completo'), 'Maria da Silva')
    await user.type(screen.getByLabelText('Email'), 'maria@exemplo.com')
    await user.type(screen.getByLabelText('Senha'), 'senhaSegura123')
    await user.click(screen.getByRole('button', { name: 'Criar conta' }))

    await waitFor(() =>
      expect(authService.cadastrar).toHaveBeenCalledWith({
        nome: 'Maria da Silva',
        email: 'maria@exemplo.com',
        telefone: '',
        senha: 'senhaSegura123',
      }),
    )
    await waitFor(() =>
      expect(navegarMock).toHaveBeenCalledWith('/login', {
        replace: true,
        state: {
          mensagemSucesso: 'Conta criada! Entre com sua senha para continuar.',
          email: 'maria@exemplo.com',
        },
      }),
    )
  })

  it('formata o telefone enquanto digita', async () => {
    const user = userEvent.setup()
    renderCadastroPage()

    await user.type(screen.getByLabelText('Telefone'), '11912345678')

    expect(screen.getByLabelText('Telefone')).toHaveValue('(11) 91234-5678')
  })

  it('mostra o erro geral vindo da API (409 email duplicado)', async () => {
    authService.cadastrar.mockRejectedValue({
      response: { status: 409, data: { detail: 'Email já cadastrado' } },
    })

    const user = userEvent.setup()
    renderCadastroPage()

    await user.type(screen.getByLabelText('Nome completo'), 'Maria da Silva')
    await user.type(screen.getByLabelText('Email'), 'maria@exemplo.com')
    await user.type(screen.getByLabelText('Senha'), 'senhaSegura123')
    await user.click(screen.getByRole('button', { name: 'Criar conta' }))

    expect(await screen.findByText('Email já cadastrado')).toBeInTheDocument()
  })
})
