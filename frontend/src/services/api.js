import axios from 'axios'
import { clearToken, getToken } from '../context/session'

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
})

api.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Só desloga globalmente se a chamada que falhou já ia autenticada — um
    // 401 de credenciais erradas em /login não deve derrubar a sessão atual.
    const eraChamadaAutenticada = Boolean(error.config?.headers?.Authorization)
    if (error.response?.status === 401 && eraChamadaAutenticada) {
      clearToken()
      window.dispatchEvent(new Event('auth:logout'))
    }
    return Promise.reject(error)
  },
)
