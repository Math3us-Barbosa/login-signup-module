import { somenteDigitos } from './formatters'

const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

export function validarNome(valor) {
  const nome = (valor ?? '').trim()
  if (!nome) return 'Nome é obrigatório'
  if (nome.length > 150) return 'Nome deve ter no máximo 150 caracteres'
  return undefined
}

export function validarEmail(valor) {
  const email = (valor ?? '').trim()
  if (!email) return 'Email é obrigatório'
  if (!EMAIL_REGEX.test(email)) return 'Email inválido'
  if (email.length > 180) return 'Email deve ter no máximo 180 caracteres'
  return undefined
}

// Telefone é opcional no backend — só valida o formato quando preenchido.
export function validarTelefone(valor) {
  const digitos = somenteDigitos(valor)
  if (!digitos) return undefined
  if (digitos.length < 10 || digitos.length > 11) {
    return 'Telefone deve conter 10 ou 11 dígitos (DDD + número)'
  }
  return undefined
}

export function validarSenhaCadastro(valor) {
  const senha = valor ?? ''
  if (!senha) return 'Senha é obrigatória'
  if (senha.length < 8 || senha.length > 72) {
    return 'Senha deve ter entre 8 e 72 caracteres'
  }
  return undefined
}

export function validarSenhaLogin(valor) {
  if (!valor) return 'Senha é obrigatória'
  return undefined
}
