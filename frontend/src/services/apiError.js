const MENSAGEM_PADRAO = 'Não foi possível completar a operação. Tente novamente.'
const MENSAGEM_REDE = 'Não foi possível conectar ao servidor. Verifique sua conexão.'

// Normaliza uma resposta de erro Axios/ProblemDetail (RFC 7807) em um formato
// único: 400 traz "errors" (mapa campo→mensagem), 401/409/500 só "detail".
export function parseApiError(error) {
  const problemDetail = error?.response?.data

  if (!problemDetail) {
    return { detail: MENSAGEM_REDE, fieldErrors: {} }
  }

  return {
    detail: problemDetail.detail || MENSAGEM_PADRAO,
    fieldErrors: problemDetail.errors ?? {},
  }
}
