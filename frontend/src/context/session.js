// Único módulo autorizado a tocar em sessionStorage — nenhum componente lê o
// token diretamente. Token fica em sessionStorage (não localStorage): some ao
// fechar a aba, reduzindo a janela de exposição a XSS.
const TOKEN_KEY = 'elo:token'

export function getToken() {
  return sessionStorage.getItem(TOKEN_KEY)
}

export function setToken(token) {
  sessionStorage.setItem(TOKEN_KEY, token)
}

export function clearToken() {
  sessionStorage.removeItem(TOKEN_KEY)
}
