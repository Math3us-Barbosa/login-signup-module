import { useLocation, useNavigate } from 'react-router-dom'
import { AuthLayout } from '../../components/AuthLayout'
import { AuthSwitchLink } from '../../components/AuthSwitchLink'
import { Button } from '../../components/Button'
import { FormAlert } from '../../components/FormAlert'
import { PasswordField } from '../../components/PasswordField'
import { TextField } from '../../components/TextField'
import { useAuth } from '../../hooks/useAuth'
import { useForm } from '../../hooks/useForm'
import { parseApiError } from '../../services/apiError'
import { validarEmail, validarSenhaLogin } from '../../utils/validators'

export default function LoginPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const { entrar } = useAuth()
  const mensagemSucesso = location.state?.mensagemSucesso

  const { values, errors, erroGeral, enviando, handleChange, handleSubmit } = useForm({
    initialValues: { email: location.state?.email ?? '', senha: '' },
    validators: { email: validarEmail, senha: validarSenhaLogin },
    onSubmit: async (dados) => {
      try {
        await entrar(dados)
        navigate('/conta', { replace: true })
      } catch (error) {
        throw parseApiError(error)
      }
    },
  })

  return (
    <AuthLayout
      eyebrow="Bem-vinda de volta"
      title="Entre na sua conta"
      subtitle="Acesse pra continuar gerenciando seus serviços."
    >
      <form className="flex flex-col gap-5" onSubmit={handleSubmit} noValidate>
        {mensagemSucesso ? <FormAlert tone="success">{mensagemSucesso}</FormAlert> : null}
        <FormAlert tone="error">{erroGeral}</FormAlert>
        <TextField
          label="Email"
          type="email"
          autoComplete="email"
          value={values.email}
          onChange={handleChange('email')}
          error={errors.email}
        />
        <PasswordField
          label="Senha"
          autoComplete="current-password"
          value={values.senha}
          onChange={handleChange('senha')}
          error={errors.senha}
        />
        <Button type="submit" loading={enviando}>
          {enviando ? 'Entrando…' : 'Entrar'}
        </Button>
        <AuthSwitchLink pergunta="Ainda não tem conta?" texto="Cadastre-se" to="/cadastro" />
      </form>
    </AuthLayout>
  )
}
