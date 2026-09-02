import { useCallback, useEffect, useState } from 'react'
import * as authService from '../services/authService'
import { AuthContext } from './authContext'
import { clearToken, getToken, setToken } from './session'

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null)
  // Deriva o estado inicial da presença do token — evita um setState
  // síncrono no efeito de montagem pra quem já chega sem sessão.
  const [carregando, setCarregando] = useState(() => Boolean(getToken()))

  const limparSessao = useCallback(() => {
    clearToken()
    setUsuario(null)
  }, [])

  // Sessão sobrevive a um refresh de página (token em sessionStorage) —
  // reidrata o usuário logado a partir do token existente ao montar.
  useEffect(() => {
    const token = getToken()
    if (!token) return
    authService
      .buscarUsuarioLogado()
      .then(setUsuario)
      .catch(() => clearToken())
      .finally(() => setCarregando(false))
  }, [])

  // Interceptor do Axios dispara isso quando um 401 vem de uma chamada
  // autenticada (token expirado/inválido) — desloga a sessão global.
  useEffect(() => {
    window.addEventListener('auth:logout', limparSessao)
    return () => window.removeEventListener('auth:logout', limparSessao)
  }, [limparSessao])

  const entrar = useCallback(async (credenciais) => {
    const { token } = await authService.login(credenciais)
    setToken(token)
    const usuarioLogado = await authService.buscarUsuarioLogado()
    setUsuario(usuarioLogado)
    return usuarioLogado
  }, [])

  // Cadastro não devolve token (a API não loga automaticamente) — só repassa
  // a chamada, quem decide o que fazer a seguir é a tela.
  const cadastrar = useCallback((dados) => authService.cadastrar(dados), [])

  const sair = useCallback(() => {
    limparSessao()
  }, [limparSessao])

  const value = {
    usuario,
    autenticado: Boolean(usuario),
    carregando,
    entrar,
    cadastrar,
    sair,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
