import { somenteDigitos } from '../utils/formatters'
import { api } from './api'

export function login({ email, senha }) {
  return api
    .post('/api/auth/login', { email: email.trim().toLowerCase(), senha })
    .then((response) => response.data)
}

export function cadastrar({ nome, email, telefone, senha }) {
  // Telefone é opcional no backend: precisa ir como null (nunca string
  // vazia), já que o @Pattern do lado de lá rejeita "" quando presente.
  const telefoneDigitos = somenteDigitos(telefone)

  return api
    .post('/api/auth/cadastro', {
      nome: nome.trim(),
      email: email.trim().toLowerCase(),
      telefone: telefoneDigitos || null,
      senha,
    })
    .then((response) => response.data)
}

export function buscarUsuarioLogado() {
  return api.get('/api/usuarios/me').then((response) => response.data)
}
